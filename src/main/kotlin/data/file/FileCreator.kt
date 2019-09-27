package data.file

import data.repository.SourceRootRepository
import model.FileType
import util.Constants
import util.Constants.COLOR_PRIMARY_FROM_ANDROID
import util.Constants.COLOR_PRIMARY_FROM_XML
import util.Constants.FCM_DIRECTORY
import util.Methods.getFileContent
import util.Methods.getInstructionFileContent

interface FileCreator {
    fun createScreenFiles(
        packageName: String,
        serviceNameText: String,
        pendingIntentText: String,
        contentTitleText: String,
        contentTextText: String,
        isNeedReadMeForInstructions: Boolean,
        module: String,
        isHavePrimaryColor: Boolean
    )
}

class FileCreatorImpl(private val sourceRootRepository: SourceRootRepository) : FileCreator {

    override fun createScreenFiles(
        packageName: String,
        serviceNameText: String,
        pendingIntentText: String,
        contentTitleText: String,
        contentTextText: String,
        isNeedReadMeForInstructions: Boolean,
        module: String,
        isHavePrimaryColor: Boolean
    ) {
        val codeSubdirectory = findCodeSubdirectory(packageName, module)
        if (codeSubdirectory != null) {
            var color = COLOR_PRIMARY_FROM_XML
            if (!isHavePrimaryColor) {
                color = COLOR_PRIMARY_FROM_ANDROID
            }
            val file = File(
                serviceNameText,
                getFileContent(
                    packageName,
                    serviceNameText,
                    pendingIntentText,
                    contentTitleText,
                    contentTextText,
                    color
                ),
                FileType.KOTLIN
            )
            val fcmDirectory = codeSubdirectory.findSubdirectory(FCM_DIRECTORY) ?: codeSubdirectory.createSubdirectory(FCM_DIRECTORY)
            fcmDirectory.addFile(file)
            if (isNeedReadMeForInstructions) {
                val file1 = File(Constants.FCM_INSTRUCTION, getInstructionFileContent(), FileType.TEXT)
                fcmDirectory.addFile(file1)
            }
        }
    }

    private fun findCodeSubdirectory(packageName: String, module: String): Directory? =
        sourceRootRepository.findCodeSourceRoot(module)?.run {
            var subdirectory = directory
            packageName.split(".").forEach {
                subdirectory = subdirectory.findSubdirectory(it) ?: subdirectory.createSubdirectory(it)
            }
            return subdirectory
        }
}
