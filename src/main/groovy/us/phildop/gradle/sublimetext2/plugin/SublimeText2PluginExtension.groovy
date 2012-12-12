package us.phildop.gradle.sublimetext2.plugin

import org.gradle.api.Project

class SublimeText2PluginExtension {

  String sublimeProjectName
  List<String> defaultFileExcludePatterns = []
  List<String> defaultFolderExcludePatterns = []
  boolean addDependencyProjects = false
  boolean generateSublimeJavaClasspath = false
  boolean generateSublimeJavaSrcpath = false
  boolean addGradleCompile = false
  boolean addSublimeLinterConfig = false
  String eclipseJavaFormatterConfigFile = null
  List<String> eclipseJavaFormatterSortImportsOrder = []
  boolean eclipseJavaFormatterRestoreLineEndings = false

  public SublimeText2PluginExtension(Project project) {
    sublimeProjectName = project.name
  }

}
