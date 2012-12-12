package us.phildop.gradle.sublimetext2

import groovy.json.JsonBuilder
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ProjectDependency
import us.phildop.gradle.classpath.ProjectClasspath

public class SublimeProject {

	Project mainProject = null
  def dependencyProjects = []

	def defaultFileExcludePatterns = []
	def defaultFolderExcludePatterns = []
  boolean addDependencyProjects = false
	boolean generateSublimeJavaClasspath = false
  boolean generateSublimeJavaSrcpath = false
	boolean generateSettings = false
  boolean addGradleCompile = false
  boolean addSublimeLinterConfig = false
  String eclipseJavaFormatterConfigFile = null
  def eclipseJavaFormatterSortImportsOrder = []
  boolean eclipseJavaFormatterRestoreLineEndings = false

  def classpathEntries = []
  def srcpathEntries = []

  SublimeProject(Project project,
  							 List<String> defaultFileExcludePatterns,
  							 List<String> defaultFolderExcludePatterns,
  							 boolean addDependencyProjects,
  							 boolean generateSublimeJavaClasspath,
                 boolean generateSublimeJavaSrcpath,
                 boolean addGradleCompile,
                 boolean addSublimeLinterConfig,
                 String eclipseJavaFormatterConfigFile,
                 List<String> eclipseJavaFormatterSortImportsOrder,
                 boolean eclipseJavaFormatterRestoreLineEndings) {
  	this.mainProject = project
  	this.defaultFileExcludePatterns = defaultFileExcludePatterns
  	this.defaultFolderExcludePatterns = defaultFolderExcludePatterns
    this.addDependencyProjects = addDependencyProjects
  	this.generateSublimeJavaClasspath = generateSublimeJavaClasspath
    this.generateSublimeJavaSrcpath = generateSublimeJavaSrcpath
  	this.generateSettings = generateSublimeJavaClasspath ||
                            generateSublimeJavaSrcpath ||
                            addSublimeLinterConfig ||
                            eclipseJavaFormatterConfigFile != null
    this.addGradleCompile = addGradleCompile
    this.addSublimeLinterConfig = addSublimeLinterConfig
    if (eclipseJavaFormatterConfigFile != null) {
      this.eclipseJavaFormatterConfigFile = (eclipseJavaFormatterConfigFile as File).getCanonicalPath()
    }

    this.eclipseJavaFormatterSortImportsOrder = eclipseJavaFormatterSortImportsOrder
    this.eclipseJavaFormatterRestoreLineEndings = eclipseJavaFormatterRestoreLineEndings

    if (generateSublimeJavaClasspath || addSublimeLinterConfig) {
      classpathEntries = new ProjectClasspath(mainProject).classpathEntries.collect {it.toString()}
    }

  	if (addDependencyProjects || generateSublimeJavaSrcpath || addSublimeLinterConfig) {
      collectDependencyProjects(project.configurations.compile)

      if (generateSublimeJavaSrcpath || addSublimeLinterConfig) {
        srcpathEntries = ([project] + dependencyProjects).collect { proj ->
          [proj.projectDir.toString(), 'src', 'main', 'java'].join(File.separator)
        }
      }
  	}
  }

  private void collectDependencyProjects(configuration) {
  	for(dep in configuration.allDependencies) {
  		if (dep instanceof ProjectDependency) {
  			dependencyProjects.add(dep.dependencyProject)
  			collectDependencyProjects(dep.projectConfiguration)
  		}
  	}
  }

  private List<Closure> getBuildSystems() {
    def gradleCmd = new File(new File(mainProject.gradle.gradleHomeDir, 'bin'), 'gradle').toString()

    [
      {
        cmd gradleCmd, 'compileJava', '-q'
        name String.format('Gradle %s', mainProject.name)
        working_dir mainProject.projectDir.toString()
        file_regex '^(...*?.java):([0-9]*)'
      }
    ]
  }

  private List<Closure> getProjectFolders() {
    def folders = []

    def addFolder = { proj ->
      folders.add {
        path proj.projectDir.toString()

        if (!defaultFileExcludePatterns.isEmpty()) {
          file_exclude_patterns defaultFileExcludePatterns
        }

        if (!defaultFolderExcludePatterns.isEmpty()) {
          folder_exclude_patterns defaultFolderExcludePatterns
        }
      }
    }

    addFolder mainProject

    if (addDependencyProjects) {
      dependencyProjects.each addFolder
    }

    folders
  }

  private List<String> getSublimeLinterArgs() {
    [
      "-sourcepath",
      srcpathEntries.join(File.pathSeparator),
      "-classpath",
      classpathEntries.join(File.pathSeparator),
      "-Xlint:all",
      "{filename}"
    ]
  }

  String toString() {
  	def json = new JsonBuilder()

    json {

      if (addGradleCompile) {
        build_systems buildSystems
      }

    	folders projectFolders

    	if (generateSettings) {
      	settings {

        	if (generateSublimeJavaClasspath) {
		    		sublimejava_classpath classpathEntries
		    	}

          if (generateSublimeJavaSrcpath) {
            sublimejava_srcpath srcpathEntries
          }

          if (addSublimeLinterConfig) {
            "SublimeLinter" {
              "Java" {
                working_directory mainProject.projectDir.toString()

                lint_args sublimeLinterArgs
              }
            }
          }

          if (eclipseJavaFormatterConfigFile != null) {
            "EclipseJavaFormatter" {
              config_file eclipseJavaFormatterConfigFile

              if (eclipseJavaFormatterSortImportsOrder != null) {
                sort_imports_order eclipseJavaFormatterSortImportsOrder
              }

              if (eclipseJavaFormatterRestoreLineEndings != null) {
                restore_line_endings eclipseJavaFormatterRestoreLineEndings
              }
            }
          }

      	}
      }

    }

    json.toPrettyString()
  }
}
