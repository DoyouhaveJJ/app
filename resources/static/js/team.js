function getmemberlst() {
    $.ajax({
        type:'get',
        url:'/getprojname',
        success:function (projname) {
            $('#proj-name').text(projname);
        }
    });
    $.ajax({
        type:'get',
        url:'/getmemberlst',
        dataType:'json',
        success:function (lst) {
            initmembertable(lst);
        }
    });
}
function initmembertable(lst) {
    $('#member-table').bootstrapTable({
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
            field: 'skill',
            align: 'center',
            title: '技能',
            formatter:skill
        }, {
            field: 'experience',
            align: 'center',
            title: '经验(年)',
        }, {
            align: 'center',
            title: '操作',
            formatter:opt
        }],
        data: lst
    });
}
function opt(value,row,index){
    let invitebtn='';
    if(row.status){
        invitebtn='<button class="btn btn-sm btn-danger mar-rgt" onclick="removemember('+row.id+')">移除成员</button>';
    }else {
        invitebtn='<button class="btn btn-sm btn-success mar-rgt" onclick="invite(' + row.id + ')" >添加成员</button>';
    }
    return '<button class="btn btn-sm btn-primary mar-rgt" onclick="turnCheck(' + row.id + ')" >项目经历</button>' +
        invitebtn;
}
function turnCheck(id) {
    let member={
        "id":id
    };
    $.ajax({
        type: 'post',
        url: '/getexp',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(member),
        success:function (data) {
            console.log(data);
            $('#proj-exp').empty();
            $('#proj-exp').append('<dt>项目名</dt>');
            let flag=true;
            for(let e of data){
                $('#proj-exp').append('<dd>'+e.name+'</dd><hr/>');
                flag=false;
            }
            if(flag)$('#proj-exp').append('<dd>'+'没有经验'+'</dd><hr/>');

        }
    });
    $('#exp-modal').modal('show');
}

function removemember(id) {
    let member = {
        "id": id
    };
    $.ajax({
        type: 'post',
        url: '/remove',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(member),
        success: function (data) {
            if(data==='success'){
                $.niftyNoty({
                    type: 'success',
                    container: 'floating',
                    title: '移除成功',
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
                    title: '移除失败',
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

function invite(id) {
    let member={
        "id":id
    };
    $.ajax({
        type:'post',
        url:'/invite',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(member),
        success:function (data) {
            if(data==='success'){
                $.niftyNoty({
                    type: 'success',
                    container: 'floating',
                    title: '添加成功',
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
                    title: '添加失败',
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
function skill(value,row,index){
    let skillstr=row.skill;
    let label='';
    if(skillstr!==null){
        let skillarr=skillstr.split(';');
        for(let skill of skillarr){
            if(skill!==''){
                label+='<label class="label label-primary label-table">'+skill+'</label>';
            }
        }
    }
    return label;
}
window.onload=function () {
    getmemberlst();
};