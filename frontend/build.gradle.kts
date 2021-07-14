import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.task.NodeTask

plugins {
  java
  // You have to specify the plugin version, for instance
  // id("com.github.node-gradle.node") version "3.0.0"
  // This works as is here because we use the plugin source
  id("com.github.node-gradle.node") version "3.1.0"
}

node {
  // Whether to download and install a specific Node.js version or not
  // If false, it will use the globally installed Node.js
  // If true, it will download node using above parameters
  // Note that npm is bundled with Node.js
  download.set(true)

  // Version of node to download and install (only used if download is true)
  // It will be unpacked in the workDir
  version.set("14.17.3")

  // Base URL for fetching node distributions
  // Only used if download is true
  // Change it if you want to use a mirror
  // Or set to null if you want to add the repository on your own.
  distBaseUrl.set("https://nodejs.org/dist")

  // The npm command executed by the npmInstall task
  // By default it is install but it can be changed to ci
  npmInstallCommand.set("install")

  // The directory where Node.js is unpacked (when download is true)
  workDir.set(file("${project.projectDir}/.gradle/nodejs"))

  // The directory where npm is installed (when a specific version is defined)
  npmWorkDir.set(file("${project.projectDir}/.gradle/npm"))

  // The Node.js project directory location
  // This is where the package.json file and node_modules directory are located
  // By default it is at the root of the current project
  nodeProjectDir.set(file("${project.projectDir}"))
}

val runTask = tasks.register<NodeTask>("reactRun") {
  dependsOn(tasks.npmInstall)
  environment.set(mutableMapOf("NODE_ENV" to "development"))
  script.set(project.file("node_modules/webpack/bin/webpack.js"))
  args.set(listOf("serve"))
}

val buildTask = tasks.register<NpmTask>("buildFrontend") {
  dependsOn(tasks.npmInstall)
  args.set(listOf("run", "build"))
}

val copyFrontend = tasks.register<Copy>("copyFrontend") {
  from("${project.rootDir}/frontend/dist")
  into("${project.rootDir}/backend/build/resources/main/static")
}

runTask {
  group = "application"
  description = "Runs this project as a React application."
}