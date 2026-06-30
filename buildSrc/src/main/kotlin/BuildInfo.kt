import net.kyori.indra.git.IndraGitExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import java.time.Instant

fun Project.getGitHash(): String =
    extensions.getByType(IndraGitExtension::class)
        .commit().orNull?.name ?: "unknown"

fun Project.getGitSHashShort(): String = getGitHash().substring(0, 12)

fun Project.getBuildCreated(): String = Instant.now().toString()