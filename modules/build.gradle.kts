/*
 * Copyright 2019-2023 CloudNetService team & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.tools.ant.filters.ReplaceTokens

plugins {
  alias(libs.plugins.juppiter) apply false
}

subprojects {
  apply(plugin = "eu.cloudnetservice.juppiter")

  configurations {
    getByName("testImplementation").extendsFrom(getByName("moduleLibrary"))
  }

  repositories {
    maven("https://repo.waterdog.dev/releases/")
    maven("https://repo.md-5.net/repository/releases/")
    maven("https://repo.md-5.net/repository/snapshots/")
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
  }

  dependencies {
    "compileOnly"(rootProject.projects.node)
    "testImplementation"(rootProject.projects.node)

    // generation for platform main classes
    "compileOnly"(rootProject.projects.ext.platformInjectSupport.platformInjectApi)
    "annotationProcessor"(rootProject.projects.ext.platformInjectSupport.platformInjectProcessor)

    // internal dependencies
    "implementation"(rootProject.libs.guava)
  }

  tasks.create<Sync>("processSources") {
    inputs.property("version", project.version)
    from(sourceSets().getByName("main").java)
    into(layout.buildDirectory.dir("src"))
    filter(ReplaceTokens::class, mapOf("tokens" to mapOf("version" to rootProject.version)))
  }

  tasks.named<JavaCompile>("compileJava") {
    dependsOn(tasks.getByName("processSources"))
    source = tasks.getByName("processSources").outputs.files.asFileTree
  }
}
