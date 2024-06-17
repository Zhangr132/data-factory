<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <style>
        @page {
            margin: 0;
            padding: 0;
        }
    </style>
</head>
<body>
<div class="header">
    <div style="padding: 10px;padding-bottom: 0px;">
        <div style="display: flex ; align-content: center ; justify-content: center">
            <img style="width: 64px ; height: 64px" src="../static/数据工厂logo.png" alt="数据工厂"/>
            <div style="margin-top: 16px ; margin-left: 8px">
                <p style="color: black ; font-family: 楷体,serif ; margin: 0">数据工厂</p>
                <p style="color: black ; font-family: 楷体,serif ; margin: 0">DataFactory</p>
            </div>
        </div>
        <hr>
        <p style="padding-bottom: 0; margin-top: 32px ; margin-bottom: 32px">尊敬的用户，您好：</p>
        <p style="text-indent: 2em; margin-bottom: 32px">您正在申请邮箱验证，您的验证码为：</p>
        <div style="width: 50% ; margin-left: 25% ; cursor: pointer ; display: flex ; background: linear-gradient(90deg,#003366,#00CCCC); height: 64px;border-radius:16px ; text-align: center">
            <p id="myCode" class="code-text">${code}</p>
        </div>
        <p style="text-indent: 2em; margin-top: 32px">为了保证您账户的安全，请勿向他人透露您的验证码。</p>
        <p style="margin-top: 32px ; margin-bottom: 128px">数据工厂开发团队</p>
    </div>
</div>
</body>
</html>
<style lang="css">
    body {
        margin: 0px;
        padding: 0px;
        font: 100% SimSun, Microsoft YaHei, Times New Roman, Verdana, Arial, Helvetica, sans-serif;
        color: #000;
        font-family: 楷体,serif;
        font-size: 20px;
    }
    .header {
        height: auto;
        width: 820px;
        min-width: 820px;
        margin: 0 auto;
        margin-top: 20px;
        border: 1px solid #eee;
    }
    .code-text {
        color: white;
        font-size: 24px;
        height: 64px;
        text-align: center;
        margin: 0 auto;
        line-height: 64px;
    }
    .footer {
        margin: 0 auto;
        z-index: 111;
        width: 800px;
        margin-top: 30px;
    }
</style>

