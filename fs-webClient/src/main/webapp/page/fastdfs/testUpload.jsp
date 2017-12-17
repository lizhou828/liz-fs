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
    缩略图：
    <br/>
    <img id ="smallImgAfterUploaded" src="" alt="">

    <br/>
    <br/>
    <br/>
    原图：
    <img id ="imgAfterUploaded" src="" alt="">
</form>

</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/scripts/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/scripts/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/scripts/ajaxFileUpload.js"></script>
<script type="text/javascript" >
    /**
     * 根据原图url 、缩略图的长、宽 获取缩略图的完整url
     * @param url
     * @param width
     * @param length
     */
    function getUrl(url, width, length) {
        if(null == url || "" == url || typeof url == "undefined"){
            return "";
        }
        var urlArray = url.split(".");
        urlArray[urlArray.length-2] =  urlArray[urlArray.length-2] + "_" + width + "x" + length ;
        return urlArray.join(".");

    }
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
//                    alert("上传成功! data=" + data + ",url = " + data[0].url);
                    $("#imgAfterUploaded").attr("src",data[0].url);
                    $("#imgAfterUploadedUrl").html(data[0].url);
                    var smallImgUrl = getUrl(data[0].url,100,100);
                    $("#smallImgAfterUploaded").attr("src",smallImgUrl);
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
