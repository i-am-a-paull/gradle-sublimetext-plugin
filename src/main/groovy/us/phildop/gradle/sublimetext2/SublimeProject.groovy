package us.phildop.gradle.sublimetext2

import groovy.json.JsonBuilder
import fj.F
import fj.data.Array
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ProjectDependency
import us.phildop.gradle.classpath.ProjectClasspath

public class SublimeProject {

	Project mainProject = null
	def projFolders = []
	def defaultFileExcludePatterns = []
	def defaultFolderExcludePatterns = []
	boolean generateSublimeJavaClasspath = false
	boolean generateSettings = false
  boolean addGradleCompile = false

  SublimeProject(Project project,
  							 List<String> defaultFileExcludePatterns, 
  							 List<String> defaultFolderExcludePatterns,
  							 boolean addDependencyProjects,
  							 boolean generateSublimeJavaClasspath,
                 boolean addGradleCompile) {
  	this.mainProject = project
  	this.defaultFileExcludePatterns = defaultFileExcludePatterns
  	this.defaultFolderExcludePatterns = defaultFolderExcludePatterns
  	this.generateSublimeJavaClasspath = generateSublimeJavaClasspath
  	this.generateSettings = generateSublimeJavaClasspath
    this.addGradleCompile = addGradleCompile

  	addFolder(project)
  	if (addDependencyProjects) {
  		addFolders(project.configurations.compile)
  	}
  }

  private void addFolders(configuration) {
  	for(dep in configuration.allDependencies) {
  		if (dep instanceof ProjectDependency) {
  			addFolder(dep.dependencyProject)
  			addFolders(dep.projectConfiguration)
  		}
  	}
  }

  void addFolder(Project project) {
  	projFolders.add({
  		path project.projectDir.toString()

  		if (!defaultFileExcludePatterns.isEmpty()) {
  			file_exclude_patterns defaultFileExcludePatterns
  		}

  		if (!defaultFolderExcludePatterns.isEmpty()) {
  			folder_exclude_patterns defaultFolderExcludePatterns
  		}
  	})
  }

  private List<Closure> getBuildSystems() {
    def gradleCmd = new File(new File(mainProject.gradle.gradleHomeDir, 'bin'), 'gradle').toString()
    def buildSystems = []
    
    buildSystems.add({
      cmd gradleCmd, 'compileJava'
      name String.format('Gradle %s', mainProject.name)
      working_dir mainProject.projectDir.toString()
    })

    buildSystems
  }

  private List<String> getClasspathEntries() {
    Array.array(new ProjectClasspath(mainProject).classpathEntries).map({it.toString()} as F).array().toList()
  }

  String toString() {
  	def json = new JsonBuilder()

    json {

      if (addGradleCompile) {
        build_systems buildSystems
      }

    	folders projFolders

    	if (generateSettings) {
      	settings {
		  
        	if (generateSublimeJavaClasspath) {
		    		sublimejava_classpath classpathEntries
		    	}
    	
      	}
      }

    }

    json.toPrettyString()
  }
}
