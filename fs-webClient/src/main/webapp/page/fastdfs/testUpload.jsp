<%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date & Time: 2017年11月13日 14时57分
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>

</head>
<body>

<form action="/fastdfs/upload.shtml" method="post" enctype="multipart/form-data">
    <input type="file" name="fsFile" />
    <input type="file" name="fsFile" />
    <input type="submit" value="表单提交上传图片" />
</form>



<form  method="post" enctype="multipart/form-data">
    <input type="file" id="fileObject" name="fsFile"/>
    <input type="button" value="ajax提交上传图片" onclick="return _ajaxFileUpload()"/>
    <br/>
    图片上传后的url: <span id="imgAfterUploadedUrl"></span>
    <br/>
    <img id ="imgAfterUploaded" src="" alt="">
</form>

</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/scripts/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/scripts/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/scripts/ajaxFileUpload.js"></script>
<script type="text/javascript" >
    function _ajaxFileUpload(){
        $.ajaxFileUpload({
            fileElementId: 'fileObject',
            url: '/fastdfs/upload.shtml',
            secureuri:false,
            dataType:'json',
            beforeSend: function (XMLHttpRequest) {
            },
            success: function (data, textStatus) {
                data = JSON.parse(data);
                if("SUCCESS" == data[0].code){
                    $("#imgAfterUploaded").attr("src",data[0].url);
                    $("#imgAfterUploadedUrl").html(data[0].url);
                    alert("上传成功! data=" + data + ",url = " + data[0].url);
                }else{
                    alert("上传失败! code=" + data[0].code);
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                alert("上传失败！服务器内部错误responseText=" + XMLHttpRequest.responseText + ",textStatus=" + textStatus + ",errorThrown=" + errorThrown);
            },
            complete : function(XMLHttpRequest, textStatus) {
            }
        });
    }
</script>
</html>
