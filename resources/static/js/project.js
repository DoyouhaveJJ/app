let creaters=[];
$.ajax({
    type:'get',
    async:false,
    url:'/getCreater',
    success:function (data) {
        creaters=data;
    }
});

function getcreater(value, row, index) {
    return creaters[row.createrId.toString()];
}
function typeparse(value, row, index) {
    if(row.type=='sm')return '小型';
    else if(row.type=='md')return'中型';
    else  return'大型';
}
function statusparse(value, row, index) {
    if(row.status==0)return '未启动';
    else if(row.status==1)return '未完成';
    else  return'已完成';
}
function operatorbtn(value,row,index) {
    return '<button class="btn btn-sm btn-primary mar-rgt" onclick="turnCheck(' + row.id + ')" >详细</button>'+
        '<button class="btn btn-sm btn-success mar-rgt" onclick="turnAlter(' + row.id + ')" >修改</button>';
}
function turnAlter(id) {
    let proj={
        "id":id
    };
    $.ajax({
        type:'post',
        contentType:'application/json;charset=UTF-8',
        url:'/getproj',
        data: JSON.stringify(proj),
        success:function (data) {
            console.log(data);
            $('#alter-name').val(data.name);
            $('#alter-desc').val(data.description);
            let arr=data.tech.split(';');
            console.log(arr);
            for(let x of arr){
                console.log(x);
                if(x=='c/c++')$('#alter-tech-1').attr("checked",true);
                else if(x=='java')$('#alter-tech-2').attr("checked",true);
                else if(x=='.net')$('#alter-tech-3').attr("checked",true);
                else if(x=='python')$('#alter-tech-4').attr('checked',true);
            }
            if(data.type=='sm')$('#alter-type-1').attr('checked',true);
            else if(data.type=='md')$('#alter-type-2').attr('checked',true);
            else if(data.type=='lg')$('#alter-type-3').attr('checked',true);
        }
    });
    $('#alter-modal').modal('show');
}
function turnCheck(id) {
    let proj={
        "id":id
    };
    $.ajax({
        type:'post',
        contentType:'application/json;charset=UTF-8',
        url:'/getproj',
        data: JSON.stringify(proj),
        success:function (data) {
            console.log(data);
            $('#more-name').text(data.name);
            $('#more-creater').text(creaters[data.createrId.toString()]);
            $('#more-start').text(data.end);
            $('#more-end').text(data.start);
            $('#more-desc').text(data.description);
            $('#more-tech').text(data.tech);
        }
    });
    $('#more-modal').modal('show');
}

function getProjLst() {

    $.ajax({
        type:"get",
        url:"/getprojlst",
        success:function (lst) {
            initProjTable(lst);
        }

    });
}

function initProjTable(lst){
    $('#project-table').bootstrapTable({
        toolbar: '#project-toolbar',
        pageSize: 5,
        pageList: [5, 10, 15],
        pagination: 'true',
        search: 'true',
        columns:[{
            field:'name',
            align:'center',
            title:'项目名'
        },{
            field:'createrId',
            align:'center',
            title:'负责人',
            formatter:getcreater
        },{
            field:'type',
            align:'center',
            title:'类型',
            formatter:typeparse
        },{
            field:'status',
            align:'center',
            title:'状态',
            formatter:statusparse
        },{
            align:'center',
            title:'操作',
            formatter:operatorbtn
        }],
        data: lst
    });
}
self.getProjLst();
function addProj() {
    let proj = {
        "name": "",
        "type": "",
        "tech": "",
        "description": ""
    };
    let flag = true;
    proj.name = $('#proj-name').val();
    proj.description = $('#proj-desc').val();
    if (proj.name != "") {
        flag = false;
    }
    if (proj.description != "") {
        flag = false;
    }
    $("input[name='proj-type']:checked").each(function () {
        proj.type = $(this).val();
        flag = false;
    });
    $("input[name='proj-tech']:checked").each(function () {
        proj.tech += $(this).val() + ';';
        flag = false;
    });

    if (!flag) {
        $.ajax({
            type: "POST",
            url: "/addNewProj",
            contentType:'application/json;charset=UTF-8',
            data: JSON.stringify(proj),
            success: function (data) {
                if (data == "success") {
                    $.niftyNoty({
                        type: 'success',
                        container: 'floating',
                        title: '新建成功',
                        closeBtn: true,
                        timer: 1000
                    });
                } else {
                    $.niftyNoty({
                        type: 'danger',
                        container: 'floating',
                        title: '新建失败,存在同名项目',
                        closeBtn: true,
                        timer: 1000,
                    });
                }
            }
        });
    }else {
        $.niftyNoty({
            type: 'danger',
            container: 'floating',
            title: '新建失败',
            message: '',
            closeBtn: true,
            timer: 1000,
        });
    }

}