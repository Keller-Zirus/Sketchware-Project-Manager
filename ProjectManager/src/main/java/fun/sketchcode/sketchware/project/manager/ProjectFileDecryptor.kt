package `fun`.sketchcode.sketchware.project.manager

import `fun`.sketchcode.sketchware.project.entity.ListFileModel
import `fun`.sketchcode.sketchware.project.entity.ProjectModel
import `fun`.sketchcode.sketchware.project.entity.ProjectPaths
import `fun`.sketchcode.sketchware.project.entity.ProjectsPaths
import `fun`.sketchcode.sketchware.project.util.gson
import `fun`.sketchcode.sketchware.project.util.toJson
import `fun`.sketchcode.util.FileUtil
import java.io.File
import java.io.RandomAccessFile
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class ProjectsManager(private val projectsPaths: ProjectPaths) {

    val projectsList: ArrayList<ProjectModel>
        get() {
            val mProjects = ArrayList<ProjectModel>()
            val mPathList = FileUtil.listDir(projectsPaths.myscListPath)
            // get data for each case
            mPathList.forEach {
                val listFileModel = decryptAndRead(it)
                mProjects.add(
                    ProjectModel(listFileModel, projectPaths(listFileModel.scId.toInt()))
                )
            }
            return mProjects
        }

    fun decryptAndRead(path: String): ListFileModel {
        return ProjectListFileReader.read(ProjectListFileUtil.decrypt(path.plus("/project")))
    }

    fun projectPaths(projectId: Int): ProjectPaths {
        val mainDir = FileUtil.externalStorageDir.plus("/sketchware")
        return ProjectPaths(
            mainDir.plus("/data/$projectId"),
            mainDir.plus("/resources/$projectId/icons"),
            mainDir.plus("/resources/$projectId/sounds"),
            mainDir.plus("/resources/$projectId/fonts"),
            mainDir.plus("/resources/$projectId/images"),
            mainDir.plus("/mysc/$projectId"),
            mainDir.plus("/mysc/list/$projectId/project")
        )
    }

    fun project(id: Int): ProjectModel {
        if (!FileUtil.isExistFile(projectsPaths.myscListPath.plus("/$id/project"))) throw ProjectNotFoundException(
            id,
            projectsPaths.myscListPath
        )
        val defaultPaths = projectPaths(id)
        val projectListFile = decryptAndRead(defaultPaths.myscListPath)
        return ProjectModel(
            projectListFile,
            defaultPaths
        )
    }

}

/**
 * @return default sketchware paths
 */
val defaultPaths: ProjectsPaths
    get() {
        return ProjectsPaths(
            FileUtil.externalStorageDir.plus("/.sketchware/data"),
            FileUtil.externalStorageDir.plus("/.sketchware/resources"),
            FileUtil.externalStorageDir.plus("/.sketchware/mysc"),
            FileUtil.externalStorageDir.plus("/.sketchware/resources/mysc/list")
        )
    }

object ProjectListFileReader {
    /**
     * @param byteArray ByteArray of decrypted list file
     * @return ListFileModel
     */
    fun read(byteArray: ByteArray): ListFileModel {
        return gson.fromJson(String(byteArray), ListFileModel::class.java)
    }

    /**
     * write project list file
     * @param path path to project list file
     * @param listFileModel ListFileModel with data to write
     */
    fun write(path: String, listFileModel: ListFileModel) {
        listFileModel.encrypt()?.let { File(path).writeBytes(it) }
    }
}

object ProjectListFileUtil {
    /**
     * Decrypt project list file, return ByteArray
     * @param path path to file
     */
    fun decrypt(path: String): ByteArray {
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val arrby = "sketchwaresecure".toByteArray()
        cipher.init(2, SecretKeySpec(arrby, "AES"), IvParameterSpec(arrby))
        val randomAccessFile = RandomAccessFile(path, "r")
        val arrby2 = ByteArray(randomAccessFile.length() as Int)
        randomAccessFile.readFully(arrby2)
        return cipher.doFinal(arrby2)
    }

    /**
     * Encrypt project list file
     * @param content ListFileModel Json
     * @return nullable ByteArray with encrypted data
     */
    fun encrypt(content: String): ByteArray? {
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        // input key for decryption
        val arrby = "sketchwaresecure".toByteArray()
        // init Cipher for decryption
        cipher.init(1, SecretKeySpec(arrby, "AES"), IvParameterSpec(arrby))
        return cipher.doFinal(content.toByteArray())
    }

}

/**
 * Encrypt ListFileModel
 * @return ByteArray of encrypted data
 */
fun ListFileModel.encrypt(): ByteArray? {
    return ProjectListFileUtil.encrypt(toJson())
}

/**
 * Delete project
 * @throws ProjectNotFoundException
 */
fun ProjectModel.delete() {
    projectPaths.delete()
}

/**
 * Delete project
 * @throws ProjectNotFoundException
 */
fun ProjectPaths.delete() {
    if (!FileUtil.isExistFile(myscListPath)) throw ProjectNotFoundException(-1, myscListPath)
    FileUtil.deleteAll(
        dataPath,
        iconsPath,
        fontsPath,
        soundsPath,
        imagesPath,
        myscPath,
        myscListPath
    )
}

fun ProjectModel.copy(newProjectPaths: ProjectPaths) {
    projectPaths.copy(newProjectPaths)
}

/**
 * Copy project
 * @param newProjectPaths new project paths
 * @throws ProjectNotFoundException
 */
fun ProjectPaths.copy(newProjectPaths: ProjectPaths) {
    if (!FileUtil.isExistFile(myscListPath)) throw ProjectNotFoundException(-1, myscListPath)
    FileUtil.copyAll(newProjectPaths.iconsPath, this.iconsPath)
    FileUtil.copyAll(newProjectPaths.fontsPath, this.fontsPath)
    FileUtil.copyAll(newProjectPaths.myscListPath, this.myscListPath)
    FileUtil.copyAll(newProjectPaths.soundsPath, this.soundsPath)
    FileUtil.copyAll(newProjectPaths.myscPath, this.myscPath)
    FileUtil.copyAll(newProjectPaths.dataPath, this.dataPath)
}


class ProjectNotFoundException(private val projectId: Int, private val listDirectory: String) :
    Exception("Project with id $projectId not found in directory: $listDirectory")
