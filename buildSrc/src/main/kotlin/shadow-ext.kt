import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.file.DuplicatesStrategy

fun ShadowJar.commonRelocate(pkg: String) {
    relocate(pkg, "${Constants.RELOCATION_BASE_PACKAGE}.$pkg")
}

fun ShadowJar.commonConfiguration() {
    mergeServiceFiles()
    // Needed for mergeServiceFiles to work properly in Shadow 9+
    filesMatching("META-INF/services/**") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}