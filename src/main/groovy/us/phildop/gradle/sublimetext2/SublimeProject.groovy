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
	def classpath = []

  SublimeProject(Project project,
  							 List<String> defaultFileExcludePatterns, 
  							 List<String> defaultFolderExcludePatterns,
  							 boolean addDependencyProjects,
  							 boolean generateSublimeJavaClasspath) {
  	this.mainProject = project
  	this.defaultFileExcludePatterns = defaultFileExcludePatterns
  	this.defaultFolderExcludePatterns = defaultFolderExcludePatterns
  	this.generateSublimeJavaClasspath = generateSublimeJavaClasspath
  	this.generateSettings = generateSublimeJavaClasspath

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

  String toString() {
  	def json = new JsonBuilder()

    json { 
    	folders projFolders

    	if (generateSettings) {
    		settings {
		    	if (generateSublimeJavaClasspath) {
		    		def classpath = new ProjectClasspath(mainProject)
		    		def cpEntries = Array.array(classpath.classpathEntries).map({it.toString()} as F).array().toList()

		    		sublimejava_classpath cpEntries
		    	}
    		}
    	}
    }

    json.toPrettyString()
  }
}
