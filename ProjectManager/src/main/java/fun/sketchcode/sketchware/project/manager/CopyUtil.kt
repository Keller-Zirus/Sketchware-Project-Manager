package `fun`.sketchcode.sketchware.project.manager

import java.io.*

object CopyUtil {
    @Throws(IOException::class)
    fun copyFolder(src: File, dest: File) {
        src.mkdirs()
        if (src.isDirectory) {

            //if directory not exists, create it
            if (!dest.exists()) {
                dest.mkdir()
                println(
                    "Directory copied from "
                            + src + "  to " + dest
                )
            }

            //list all the directory contents
            val files = src.list()
            for (file in files) {
                //construct the src and dest file structure
                val srcFile = File(src, file)
                val destFile = File(dest, file)
                //recursive copy
                copyFolder(srcFile, destFile)
            }
        } else {
            //if file, then copy it
            //Use bytes stream to support all file types
            val `in`: InputStream = FileInputStream(src)
            val out: OutputStream = FileOutputStream(dest)
            val buffer = ByteArray(1024)
            var length: Int
            //copy the file content in bytes
            while (`in`.read(buffer).also { length = it } > 0) {
                out.write(buffer, 0, length)
            }
            `in`.close()
            out.close()
            println("File copied from $src to $dest")
        }
    }
}