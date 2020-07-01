package `fun`.sketchcode.sketchware.project.entity

import com.google.gson.annotations.SerializedName


data class ListFileModel(
    @SerializedName("sketchware_ver")
    val sketchwareVer: Double,
    @SerializedName("sc_id")
    val scId: String,
    @SerializedName("my_sc_pkg_name")
    val myScPkgName: String,
    @SerializedName("my_app_name")
    val myAppName: String,
    @SerializedName("sc_ver_code")
    val scVerCode: String,
    @SerializedName("color_control_highlight")
    val colorControlHighLight: Double,
    @SerializedName("color_primary")
    val colorPrimary: Double,
    @SerializedName("color_accent")
    val colorAccent: Double,
    @SerializedName("my_sc_reg_dt")
    val myScRegDt: String,
    @SerializedName("color_primary_dark")
    val colorPrimaryDark: Double,
    @SerializedName("color_control_normal")
    val colorControlNormal: Double,
    @SerializedName("sc_ver_name")
    val scVerName: String,
    @SerializedName("my_ws_name")
    val myWsName: String,
    @SerializedName("custom_icon")
    val customIcon: Boolean
)

data class ProjectModel(
    val listFileModel: ListFileModel,
    val projectPaths: ProjectPaths
)

data class ProjectPaths(
    val dataPath: String,
    val iconsPath: String,
    val soundsPath: String,
    val fontsPath: String,
    val imagesPath: String,
    val myscPath: String,
    val myscListPath: String
)

data class ProjectsPaths(
    val dataPath: String,
    val resourcesPath: String,
    val myscPath: String,
    val myscListPath: String
)