<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
</head>
<body>

<section class="content">
    <div class="row">
        <div class="col-xs-12">

            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">接口名称：${doc.name}</h3>

                    <h3 class="box-title">接口描述：${doc.desc}</h3>
                </div>

                <!-- /.box-header -->
                <div class="box-body table-responsive">
                    <h4> 请求接口</h4>
                    <center><h5> ${doc.req.className}</h5></center>
                    <table id="example1" class="table table-bordered table-striped table-condensed">
                        <thead>
                        <tr>
                            <th>参数名</th>
                            <th>类型</th>
                            <th>描述</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list doc.req.params as param>
                        <tr>
                            <td>${param.name}</td>
                            <td>${param.type}</td>
                            <td>${param.desc}</td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <!-- /.box-body -->
                <h4>返回接口</h4>
            <#list doc.respList as resp>
                <div class="box-body table-responsive">

                    <center><h5>${resp.className}</h5></center>
                    <table id="example1" class="table table-bordered table-striped table-condensed ">
                        <thead>
                        <tr>
                            <th>参数名</th>
                            <th>类型</th>
                            <th>描述</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list resp.params as param>
                            <tr>
                                <td>${param.name}</td>
                                <td>${param.type?html}</td>
                                <td>${param.desc}</td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
            </#list>
            </div>
            <!-- /.box -->
        </div>
    </div>

</section>

</body>
</html>