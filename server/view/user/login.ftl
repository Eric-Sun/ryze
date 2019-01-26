<!DOCTYPE html>
<html lang="zh-cn">
<head>

    <link rel="stylesheet" href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
</head>
<body>
<form action="/user/login">
    手机号 <input type="text" name="mobile"/> <br>
    密码 <input type="text" name="password"/>   <br>
    <input type="submit" value="登陆"/>

</form>
<button onclick="javascript:window.location.href='/user/register'">注册</button>
</body>
</html>