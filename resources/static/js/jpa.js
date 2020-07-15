function doLogout() {
    $.ajax({
        type:"POST",
        url:"doLogout",
        contentType:'application/json;charset=UTF-8',
        success:function (data) {
            if(data==="success"){
                $.niftyNoty({
                    type: 'success',
                    container : 'floating',
                    title : '注销成功',
                    message : '',
                    closeBtn : true,
                    timer : 1000,
                    onHide:function () {
                        location = "/"
                    }
                });
            }
        }
    })
}

function initAdd() {

    // SUMMERNOTE
    // =================================================================
    // Require Summernote
    // http://hackerwins.github.io/summernote/
    // =================================================================
    $('#Description_md, #demo-summernote-full-width').summernote({
        height : '600px'
    });

    $('#Available').niftyCheck('toggleOn')
    // DROPZONE.JS
    // =================================================================
    // Require Dropzone
    // http://www.dropzonejs.com/
    // =================================================================
    $('#AIFile-dropzone').dropzone({
        addRemoveLinks: true,
        maxFiles: 1,
        maxFilesize: 4096, //MB
        acceptedFiles: ".jpg,.zip,.rar",
        dictDefaultMessage: '拖动文件至此或者点击上传',
        dictMaxFilesExceeded: "您最多只能上传1个文件！",
        dictResponseError: '文件上传失败!',

        dictInvalidFileType: "文件类型只能是zip,rar压缩文件。",
        dictFallbackMessage: "浏览器不受支持",
        dictFileTooBig: "文件过大上传文件最大支持.",
        dictRemoveLinks: "删除",
        dictCancelUpload: "取消",

        init: function () {
            var myDropzone = this;
            myDropzone.on('maxfilesexceeded', function (file) {
                this.removeAllFiles();
                this.addFile(file);
            });
            myDropzone.on('removedfile', function (file) {
                console.log("移除文件")
            });
            myDropzone.on('error', function (file) {
                console.log("出现错误")
            });
            myDropzone.on('success', function (file) {
                console.log("成功")
            });
            myDropzone.on('addedfile', function (file) {
                console.log("添加文件")
            });
        }
    });

    $('#AIPaper-dropzone').dropzone({
        addRemoveLinks: true,
        maxFiles: 1,
        acceptedFiles: ".doc,.docx",
        dictDefaultMessage: '拖动文件至此或者点击上传',
        dictMaxFilesExceeded: "您最多只能上传1个文件！",
        dictResponseError: '文件上传失败!',
        dictInvalidFileType: "文件类型只能是doc,docx。",
        dictFallbackMessage: "浏览器不受支持",
        dictFileTooBig: "文件过大上传文件最大支持.",
        dictRemoveLinks: "删除",
        dictCancelUpload: "取消",

        init: function () {
            var myDropzone = this;
            myDropzone.on('maxfilesexceeded', function (file) {
                this.removeAllFiles();
                this.addFile(file);
            });
            myDropzone.on('removedfile', function (file) {
                console.log("移除文件")
            });
            myDropzone.on('error', function (file) {
                console.log("出现错误")
            });
            myDropzone.on('success', function (file) {
                console.log("成功")
            });
            myDropzone.on('addedfile', function (file) {
                console.log("添加文件")
            });
        }
    });
    // SUMMERNOTE
    // =================================================================
    // Require Summernote
    // http://hackerwins.github.io/summernote/
    // =================================================================

}


function doLogin() {
    var user = {
        "account":"",
        "password":""
    };
    if(checkAccount(user)&&checkPassword(user)){
        $.ajax({
            type:"POST",
            url:"doLogin",
            contentType:'application/json;charset=UTF-8',
            data:JSON.stringify(user),
            success:function (data) {
                if(data ==="success"){
                    $.niftyNoty({
                        type: 'success',
                        container : 'floating',
                        title : '登录成功',
                        message : '',
                        closeBtn : true,
                        timer : 1000,
                        onHidden:function () {
                            location = "/"
                        }

                    });
                }
                else{
                    $.niftyNoty({
                        type: 'failure',
                        container : 'floating',
                        title : '登录失败',
                        message : '账号或密码错误',
                        closeBtn : true,
                        timer : 1000
                    });
                }
            }

        })
    }
}


function checkAccount(user) {
    var account = $("#account").val();
    if(account === ""){
        $.niftyNoty({
            type: 'danger',
            container : 'floating',
            title : '请输入账号',
            message : '',
            closeBtn : true,
            timer : 1000
        });
        return false;
    }
    user.account = account;
    return true;
}

function checkPassword(user) {
    var password = $("#password").val();
    if(password === ""){
        $.niftyNoty({
            type: 'danger',
            container : 'floating',
            title : '请输入密码',
            message : '',
            closeBtn : true,
            timer : 1000
        });
        return false;
    }
    user.password = password;
    return true;
}

function checkPassword2() {
    var password = $("#password2").val();
    if(password === ""){
        $.niftyNoty({
            type: 'danger',
            container : 'floating',
            title : '请输入确认密码',
            message : '',
            closeBtn : true,
            timer : 1000
        });
        return false;
    }
    return true;
}

function doRegister() {
    var user = {
        "account":"",
        "password":"",
        "name":"",
        "skill":"",
        "experience":"",
        "role":0
    };
    var flag=0;
    $("input[name='skill']:checked").each(function () {
            user.skill+=$(this).val()+';';
            flag=1;
    });
    if(flag==0){
        $.niftyNoty({
            type: 'danger',
            container : 'floating',
            title : '请选择至少一项技能',
            message : '',
            closeBtn : true,
            timer : 1000
        });
    }
    $("input[name='role']:checked").each(function () {
        user.role=$(this).val();
    });
    if(checkAccount(user)&&checkPassword(user)&&checkPassword2()&&checkPP()&&checkName(user)&&flag){

        user.experience=$('#experience').val();

        $.ajax({
            type:"POST",
            url:"doRegister",
            contentType:'application/json;charset=UTF-8',
            data:JSON.stringify(user),
            success:function (data) {
                if(data ==="success"){
                    $.niftyNoty({
                        type: 'success',
                        container : 'floating',
                        title : '注册成功',
                        message : '',
                        closeBtn : true,
                        timer : 1000,
                        onHidden:function () {
                            location = "/";
                        }

                    });
                }
                else{
                    $.niftyNoty({
                        type: 'failure',
                        container : 'floating',
                        title : '注册失败',
                        message : '账号重复',
                        closeBtn : true,
                        timer : 1500
                    });
                }
            }

        })
    }
}

function checkPP() {
    if($("#password").val() !== $("#password2").val()){
        $.niftyNoty({
            type: 'danger',
            container : 'floating',
            title : '两次密码不匹配',
            message : '',
            closeBtn : true,
            timer : 1000
        });
        return false;
    }
    return true
}
// function checkEmail(user) {
//     var email = $("#email").val();
//     if(email === ""){
//         $.niftyNoty({
//             type: 'danger',
//             container : 'floating',
//             title : '请输入邮箱地址',
//             message : '',
//             closeBtn : true,
//             timer : 1000
//         });
//         return false;
//     }
//     if(EmailCheck(email)){
//         user.email=email;
//         return true;
//
//     }else {
//         $.niftyNoty({
//             type: 'purple',
//             container: 'floating',
//             title: '邮箱格式不对',
//             message: '',
//             closeBtn: true,
//             timer: 1000
//         });
//         return false;
//     }
// }
//
// function EmailCheck(email){
//     var t  = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
//     return t.test(email);
// }

function checkName(user) {
    var name = $("#name").val();
    if(name === ""){
        $.niftyNoty({
            type: 'danger',
            container : 'floating',
            title : '请输入昵称',
            message : '',
            closeBtn : true,
            timer : 1000
        });
        return false;
    }
    user.name = name;
    return true;
}