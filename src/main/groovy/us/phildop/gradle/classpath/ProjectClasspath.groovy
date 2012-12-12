package us.phildop.gradle.classpath

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency

class ProjectClasspath {

  private def projectDirs = []
  private def jars = []

  ProjectClasspath(project) {
    projectDirs.add(getProjectClassesDir(project))
    collectClasspathEntries(project.configurations.compile)
  }

  File[] getClasspathEntries() {
    def files = []
    files.addAll(projectDirs)
    files.addAll(jars)
    files as File[]
  }

  private void collectClasspathEntries(configuration) {
    def jarDeps = []

    for(dep in configuration.allDependencies) {
      if (dep instanceof ProjectDependency) {
        projectDirs.add(getProjectClassesDir(dep.dependencyProject))
        collectClasspathEntries(dep.projectConfiguration)
      }
      else {
        jarDeps.add(dep)
      }
    }

    for(file in configuration.fileCollection(jarDeps as Dependency[]).files) {
      jars.add(file)
    }
  }

  private File getProjectClassesDir(project) {
    new File(new File(project.buildDir, "classes"), "main")
  }

}
