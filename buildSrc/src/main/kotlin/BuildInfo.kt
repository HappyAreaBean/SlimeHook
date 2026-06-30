import java.text.SimpleDateFormat
import java.util.Date
import net.kyori.indra.git.IndraGitExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

fun Project.getGitHash(): String =
    extensions.getByType(IndraGitExtension::class)
        .commit()
        .get()
        .name()
        .toString()
        .trim()

fun Project.getGitSHashShort(): String = getGitHash().substring(0, 12)

fun Project.getDate(): String =
    SimpleDateFormat("yyyy.MM.dd").format(Date())