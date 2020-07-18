function getmemberlst() {
    $.ajax({
        type:'get',
        url:'/getRole',
        dataType:'json',
        success:function (lst) {
            initmembertable(lst);
        }
    });
}
function initmembertable(lst) {
    $('#role-table').bootstrapTable({
        toolbar: '#project-toolbar',
        pageSize: 5,
        pageList: [5, 10, 15],
        pagination: 'true',
        search: 'true',
        columns: [{
            field: 'name',
            align: 'center',
            title: '姓名'
        }, {
            field: 'role',
            align: 'center',
            title: '权限',
        }, {
            align: 'center',
            title: '操作',
            formatter:opt
        }],
        data: lst
    });
}
function opt(value,row,index){

    return '<button class="btn btn-sm btn-primary mar-rgt" onclick="turnCheck(' + row.id + ')" >修改权限</button>';
}
function turnCheck(id) {
    let member={
            "id":id
        };
        $.ajax({
            type:'post',
            url:'/changeRole',
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(member),
            success:function (data) {
                if(data==='success'){
                    $.niftyNoty({
                        type: 'success',
                        container: 'floating',
                        title: '修改成功',
                        closeBtn: true,
                        timer: 1000,
                        onHidden:function () {
                            window.location.reload();
                        }
                    });
                }else {
                    $.niftyNoty({
                        type: 'danger',
                        container: 'floating',
                        title: '修改失败',
                        closeBtn: true,
                        timer: 1000,
                        onHidden:function () {
                            window.location.reload();
                        }
                    });
                }
            }
        });
}


window.onload=function () {
    getmemberlst();
};