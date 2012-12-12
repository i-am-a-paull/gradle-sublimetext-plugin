package us.phildop.gradle.sublimetext2.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import us.phildop.gradle.sublimetext2.SublimeProject

class SublimeText2Task extends DefaultTask {

  String sublimeProjectName
  List<String> defaultFileExcludePatterns
  List<String> defaultFolderExcludePatterns
  boolean addDependencyProjects
  boolean generateSublimeJavaClasspath
  boolean generateSublimeJavaSrcpath
  boolean addGradleCompile
  boolean addSublimeLinterConfig
  String eclipseJavaFormatterConfigFile
  List<String> eclipseJavaFormatterSortImportsOrder
  boolean eclipseJavaFormatterRestoreLineEndings


  @TaskAction
  void run() {
    def st2Proj = new SublimeProject(project,
                                     getDefaultFileExcludePatterns(),
                                     getDefaultFolderExcludePatterns(),
                                     getAddDependencyProjects(),
                                     getGenerateSublimeJavaClasspath(),
                                     getGenerateSublimeJavaSrcpath(),
                                     getAddGradleCompile(),
                                     getAddSublimeLinterConfig(),
                                     getEclipseJavaFormatterConfigFile(),
                                     getEclipseJavaFormatterSortImportsOrder(),
                                     getEclipseJavaFormatterRestoreLineEndings())
    File destination = new File(project.projectDir, String.format("%s.sublime-project", getSublimeProjectName()))
    destination.write st2Proj.toString()
  }

}
