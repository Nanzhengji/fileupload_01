<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<title>文件上传</title>
</head>
<body>
    <!-- 文件上传表单的提交方式必须是“post” 编码类型必须为：enctype="multipart/form-data" -->
    <form action="fileupload" method="post" enctype="multipart/form-data">
    选择文件:<input type="file" name="myFile"><br/>
    <input type="submit" value="上传文件"><br/>
    <input type="reset" value="重置">
    </form>

</body>
</html>