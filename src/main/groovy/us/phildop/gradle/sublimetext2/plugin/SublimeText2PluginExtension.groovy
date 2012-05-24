package us.phildop.gradle.sublimetext2.plugin

import org.gradle.api.Project

class SublimeText2PluginExtension {

  String sublimeProjectName
  List<String> defaultFileExcludePatterns = []
  List<String> defaultFolderExcludePatterns = []
  boolean addDependencyProjects = false
  boolean generateSublimeJavaClasspath = false

  public SublimeText2PluginExtension(Project project) {
    sublimeProjectName = project.name
  }

}