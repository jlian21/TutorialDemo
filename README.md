# TutorialDemo

启动Application：
1. 打开local mongoDB
    brew services start mongodb-community@4.4
2. cd ../TutorialDemo/file-transfer
3. ./mvnw spring-boot:run

使用：
1. 上传文件
    点击Choose File，选择需要上传的文件，再点击Upload,进度条运行完毕后显示Uploaded the file successfully即上传文件成功。
2. 下载文件
    经第一步上传成功的文件的名称将出现在下方的List of Files方框中。点击想要下载的一行中的文件名即可下载文件。
3. 删除文件
    点击List of Files任意一行右边的Delete按钮即可删除对应文件。
    此处需要点击两次Delete才能成功删除文件（unfixed bug here）

Remain parts:
1. Delete all files function
2. Search file by name function
3. Pagination
4. Remote MongoDB repository
...
