<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>i客保赠险任务调度</title>
    <link rel="shortcut icon" href="/static/icon.jpg" type="image/x-icon"></link>
    <link rel="stylesheet" href="https://unpkg.com/element-ui@2.0.5/lib/theme-chalk/index.css">
    <script src="https://unpkg.com/vue/dist/vue.js"></script>
    <script src="http://cdn.bootcss.com/vue-resource/1.3.4/vue-resource.js"></script>
    <script src="https://unpkg.com/element-ui@2.0.5/lib/index.js"></script>
    <script src="../static/axios.min.js"></script>

    <style>
        #top {
            background:#F49C00;
            padding:5px;
            overflow:hidden
        }
    </style>

</head>
<body>
<div id="test">

    <div id="top">
        <el-button type="text" @click="search" style="color:white">查询</el-button>
        <el-button type="text" @click="handleadd" style="color:white">添加</el-button>
        </span>
    </div>

    <br/>

    <div style="margin-top:15px">

        <el-table
                ref="testTable"
                :data="tableData"
                style="width:100%"
                border
        >
            <el-table-column
                    prop="id"
                    label="任务ID"
                    sortable
                    show-overflow-tooltip>
            </el-table-column>
            <el-table-column
                    prop="jobName"
                    label="任务名称"
                    sortable
                    show-overflow-tooltip>
            </el-table-column>

            <el-table-column
                    prop="jobGroup"
                    label="任务所在组"
                    sortable>
            </el-table-column>

            <el-table-column
                    prop="jobClassPath"
                    label="任务类名"
                    sortable>
            </el-table-column>

            <el-table-column
                    prop="jobCron"
                    label="表达式"
                    sortable>
            </el-table-column>

            <el-table-column
                    prop=jobStatus
                    label="任务状态(0运行,1暂停,-1开发中)"
                    sortable>
            </el-table-column>

            <el-table-column
                    prop="jobDescribe"
                    label="任务描述"
                    sortable>
            </el-table-column>

            <el-table-column label="操作" width="300">
                <template scope="scope">
                    <el-button
                            size="small"
                            type="warning"
                            @click="handlePause(scope.$index, scope.row)">暂停</el-button>

                    <el-button
                            size="small"
                            type="info"
                            @click="handleResume(scope.$index, scope.row)">恢复</el-button>

                    <el-button
                            size="small"
                            type="danger"
                            @click="handleDelete(scope.$index, scope.row)">删除</el-button>

                    <el-button
                            size="small"
                            type="success"
                            @click="handleUpdate(scope.$index, scope.row)">修改</el-button>
                </template>
            </el-table-column>
        </el-table>

        <div align="center">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size="pagesize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalCount">
            </el-pagination>
        </div>
    </div>

   <el-dialog title="添加任务" :visible.sync="dialogFormVisible">
        <el-form :model="form">
            <el-form-item label="任务名称" label-width="120px" style="width:35%">
                <el-input v-model="form.jobName" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="任务分组" label-width="120px" style="width:35%">
                <el-input v-model="form.jobGroup" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="表达式" label-width="120px" style="width:35%">
                <el-input v-model="form.cronExpression" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="任务全限定类名" label-width="120px" style="width:35%">
                <el-input v-model="form.jobClassPath" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="任务描述" label-width="120px" style="width:35%">
                <el-input v-model="form.jobDescribe" auto-complete="off"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="add">确 定</el-button>
        </div>
    </el-dialog>

    <el-dialog title="修改任务" :visible.sync="updateFormVisible">
        <el-form :model="updateform">
            <el-form-item label="表达式" label-width="120px" style="width:35%">

                <el-input v-model="updateform.cronExpression" auto-complete="off"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="updateFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="update">确 定</el-button>
        </div>
    </el-dialog>

</div>

<footer align="center">
    <p>&copy; i客保赠险任务调度</p>
</footer>

<script>
    var vue = new Vue({
        el:"#test",
        data: {
            //表格当前页数据
            tableData: [],

            //请求的URL
            url:'task/queryTask',

            //默认每页数据量
            pagesize: 10,

            //当前页码
            currentPage: 1,

            //查询的页码
            start: 1,

            //默认数据总数
            totalCount: 1000,

            //添加对话框默认可见性
            dialogFormVisible: false,

            //修改对话框默认可见性
            updateFormVisible: false,

            //提交的表单
            form: {
                jobName: '',
                jobGroup: '',
                cronExpression: '',
            },

            updateform: {
                jobName: '',
                jobGroup: '',
                cronExpression: '',
            },
        },

        methods: {

            //从服务器读取数据
            loadData: function(pageNum, pageSize){
                this.$http.get('task/queryTask?' + 'pageNum=' +  pageNum + '&pageSize=' + pageSize).then(function(res){
                    //console.log(res)
                    //console.log(res.body.list)
                    this.tableData = res.body.list;
                    this.totalCount = res.body.number;
                },function(){
                    //console.log('failed');
                });
            },

            //单行删除
            handleDelete: function(index, row) {
                this.$http.post('job/changeStatus?id='+row.id+"&statusCode=2").then(function(res){
                    this.loadData( this.currentPage, this.pagesize);
                    alert(res.body.msg);
                },function(){
                    //console.log('failed');
                });
            },

            //暂停任务
            handlePause: function(index, row){
                this.$http.post('job/changeStatus?id='+row.id+"&statusCode=1").then(function(res){
                    this.loadData( this.currentPage, this.pagesize);
                    alert(res.body.msg);
                },function(){
                    //console.log('failed');
                });
            },

            //恢复任务
            handleResume: function(index, row){
                this.$http.get('job/changeStatus?id='+row.id+"&statusCode=0").then(function(res){
                    this.loadData( this.currentPage, this.pagesize);
                    alert(res.body.msg);
                },function(){
                    //console.log('failed');
                });
            },

            //搜索
            search: function(){
                this.loadData(this.currentPage, this.pagesize);
            },
            //弹出对话框
            handleadd: function(){
                this.dialogFormVisible = true;
            },

            //添加
            add: function(){
                /*this.$http.post('job/addJob',{"jobName":this.form.jobName,"jobGroup":this.form.jobGroup,"jobCron":this.form.jobCron,"jobClassPath":this.form.jobClassPath,"jobDescribe":this.form.jobDescribe},{emulateJSON: true}).then(function(res){
                    this.loadData(this.currentPage, this.pagesize);
                    this.dialogFormVisible = false;
                    alert(res.body.msg);
                },function(){
                    //console.log('failed');
                });*/
                axios.post('job/addJob',{"jobName":this.form.jobName,"jobGroup":this.form.jobGroup,"jobCron":this.form.cronExpression,"jobClassPath":this.form.jobClassPath,"jobDescribe":this.form.jobDescribe},{emulateJSON: true}).then(res => {
                    this.loadData(this.currentPage, this.pagesize);
                     this.dialogFormVisible = false;
                this.$message.success('恭喜已添加');
                alert(res.data.msg);
                });
            },

            //更新
            handleUpdate: function(index, row){
                //console.log(row)
                this.updateFormVisible = true;
                this.id= row.id;
                this.updateform.jobName = row.jobName;
                this.updateform.jobGroup = row.jobGroup;
            },

            //更新任务
            update: function(){
                //console.log(this.updateform.cronExpression);
                this.$http.get
                ('job/updateJob?id='+this.id+"&cron="+this.updateform.cronExpression).then(function(res){
                    this.loadData(this.currentPage, this.pagesize);
                    this.updateFormVisible = false;
                    alert(res.body.msg)
                },function(){
                    //console.log('failed');
                });

            },

            //每页显示数据量变更
            handleSizeChange: function(val) {
                this.pagesize = val;
                this.loadData(this.currentPage, this.pagesize);
            },

            //页码变更
            handleCurrentChange: function(val) {
                this.currentPage = val;
                this.loadData(this.currentPage, this.pagesize);
            },

        },


    });

    //载入数据
    vue.loadData(vue.currentPage, vue.pagesize);
</script>

</body>
</html>