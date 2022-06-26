
function goBasicInfo() {

    let type        = "get";
    let data        = "";
    let url         = REST_API_URL + "/basicInfo/list";
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let async       = false;
    let callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( data => {
        if(data.basicInfoList.length == 0) {
            e1Exception();
        } else {
            localStorage.setItem('basicInfo', JSON.stringify(data.basicInfoList[0]));
            localStorage.setItem('isUpdate', 'true');
            localStorage.removeItem('basicInfoView1Save');
            localStorage.removeItem('basicInfoView2Save');
            localStorage.removeItem('basicInfoView3Save');
            location.href = "/admin/basicInfo/1";
        }
    })
}


function setBasicInfoView1 () {

    let isUpdate = localStorage.isUpdate;
    let view1Save = localStorage.basicInfoView1Save;

    if (isUpdate == 'true' && view1Save != 'true') {
        let basicInfoData = JSON.parse(localStorage.basicInfo);
        $("#id").val(basicInfoData.id);
        $("#name").val(basicInfoData.name);
        $("#senior_pastor_name").val(basicInfoData.senior_pastor_name);
        $("#callNumber").val(basicInfoData.callNumber);
        $("#basicAddress").val(basicInfoData.basicAddress);
        $("#detailAddress").val(basicInfoData.detailAddress);
        $("#youtubeURL").val(basicInfoData.youtubeURL);
        $("#kakaoPage").val(basicInfoData.kakaoPage);
        $("#instagram").val(basicInfoData.instagram);
        $("#blog").val(basicInfoData.blog);
    }
    else if ( view1Save == 'true' )
    {
        let basicInfoData = JSON.parse(localStorage.newBasicInfo);
        $("#id").val(basicInfoData.id);
        $("#name").val(basicInfoData.name);
        $("#senior_pastor_name").val(basicInfoData.senior_pastor_name);
        $("#callNumber").val(basicInfoData.callNumber);
        $("#basicAddress").val(basicInfoData.basicAddress);
        $("#detailAddress").val(basicInfoData.detailAddress);
        $("#youtubeURL").val(basicInfoData.youtubeURL);
        $("#kakaoPage").val(basicInfoData.kakaoPage);
        $("#instagram").val(basicInfoData.instagram);
        $("#blog").val(basicInfoData.blog);
    }
}


function setBasicInfoView2 () {

    let isUpdate = localStorage.isUpdate;
    let view2Save = localStorage.basicInfoView2Save;

    if (isUpdate == 'true' && view2Save != 'true') {
        let basicInfoData = JSON.parse(localStorage.basicInfo);
        $("#basicInfo").val(basicInfoData.basicInfo);
        $('#contents').summernote('code', basicInfoData.detailInfo);
    }
    else if ( view2Save == 'true' )
    {
        let basicInfoData = JSON.parse(localStorage.newBasicInfo);
        $("#basicInfo").val(basicInfoData.basicInfo);
        $('#contents').summernote('code', basicInfoData.contents);
    }
}

function setBasicInfoView3 () {

    let isUpdate = localStorage.isUpdate
    if (isUpdate == 'true') {
        $("#updateBtn").removeClass("hide");
        $("#goAdminBtn").removeClass("hide");
        $("#saveBtn").addClass("hide");
    }
}


function view1Validation () {
    let result = true;
    if($("#name").val() == "") {
        $("#nameLabel1").removeClass("hide");
        $("#nameLabel2").addClass("hide");
        result = false;
    }
    else {
        $("#nameLabel2").removeClass("hide");
        $("#nameLabel1").addClass("hide");
    }
    if($("#senior_pastor_name").val() == "") {
        $("#senior_pastor_name_Label1").removeClass("hide");
        $("#senior_pastor_name_Label2").addClass("hide");
        result = false;
    }
    else {
        $("#senior_pastor_name_Label2").removeClass("hide");
        $("#senior_pastor_name_Label1").addClass("hide");
    }
    if($("#callNumber").val() == "") {
        $("#callNumberLabel1").removeClass("hide");
        $("#callNumberLabel2").addClass("hide");
        result = false;
    }
    else {
        $("#callNumberLabel2").removeClass("hide");
        $("#callNumberLabel1").addClass("hide");
    }
    if($("#basicAddress").val() == "") {
        $("#basicAddressLabel1").removeClass("hide");
        $("#basicAddressLabel2").addClass("hide");
        result = false;
    }
    else {
        $("#basicAddressLabel2").removeClass("hide");
        $("#basicAddressLabel1").addClass("hide");
    }
    return result;
}

function view2Validation () {
    let result = true;

    if($("#basicInfo").val() == "") {
        $("#basicInfoLabel1").removeClass("hide");
        $("#basicInfoLabel2").addClass("hide");
        result = false;
    }
    else {
        $("#basicInfoLabel2").removeClass("hide");
        $("#basicInfoLabel1").addClass("hide");
    }
    if($("#contents").val() == "") {
        $("#detailInfoLabel1").removeClass("hide");
        $("#detailInfoLabel2").addClass("hide");
        result = false;
    }
    else {
        $("#detailInfoLabel2").removeClass("hide");
        $("#detailInfoLabel1").addClass("hide");
    }
    return result;
}

function goView1 (){
    if( view2Validation() ) {
        let newBasicInfo = JSON.parse(localStorage.newBasicInfo);
        newBasicInfo.basicInfo = $("#basicInfo").val();
        newBasicInfo.contents =  $("#contents").val();

        localStorage.setItem("newBasicInfo", JSON.stringify(newBasicInfo));
        localStorage.setItem("basicInfoView2Save", "true");
        location.href = "/admin/basicInfo/1";
    }
}

function goView2(prePage){

    if(prePage == 3)
    {
        let crm = confirm("이전 페이지로 이동하면 모든 파일이 삭제됩니다. 이동하시겠습니까")
        if(crm)
            location.href="/admin/basicInfo/2";
    }
    else if (prePage == 1)
    {
        if(view1Validation ()) {

            if(localStorage.newBasicInfo)
            {
                let newBasicInfo = JSON.parse(localStorage.newBasicInfo);
                newBasicInfo.id = $("#id").val();
                newBasicInfo.name = $("#name").val();
                newBasicInfo.senior_pastor_name = $("#senior_pastor_name").val();
                newBasicInfo.callNumber = $("#callNumber").val();
                newBasicInfo.basicAddress = $("#basicAddress").val();
                newBasicInfo.detailAddress = $("#detailAddress").val();
                newBasicInfo.youtubeURL = $("#youtubeURL").val();
                newBasicInfo.kakaoPage = $("#kakaoPage").val();
                newBasicInfo.blog = $("#blog").val();
                newBasicInfo.basicAddress = $("#basicAddress").val();
                localStorage.setItem("newBasicInfo", JSON.stringify(newBasicInfo));
            }
            else
            {
                let newBasicInfo = {
                    id : $("#id").val(),
                    name :  $("#name").val(),
                    senior_pastor_name :  $("#senior_pastor_name").val(),
                    callNumber : $("#callNumber").val(),
                    basicAddress :  $("#basicAddress").val(),
                    detailAddress :  $("#detailAddress").val(),
                    youtubeURL :  $("#youtubeURL").val(),
                    kakaoPage :  $("#kakaoPage").val(),
                    blog :  $("#blog").val(),
                    basicAddress :  $("#basicAddress").val(),
                };
                localStorage.setItem("newBasicInfo", JSON.stringify(newBasicInfo));
            }
            localStorage.setItem("basicInfoView1Save", "true");
            location.href = "/admin/basicInfo/2";
        }
    }
}


function goView3(){

    if(view2Validation ()) {
        let newBasicInfo = JSON.parse(localStorage.newBasicInfo);
        newBasicInfo.basicInfo = $("#basicInfo").val();
        newBasicInfo.contents = $("#contents").val();

        localStorage.setItem("newBasicInfo", JSON.stringify(newBasicInfo));
        localStorage.setItem("basicInfoView2Save", "true");
        location.href = "/admin/basicInfo/3";
    }
}

function goIndex() {
    localStorage.clear();
    location.href="/admin";
}


function basicInfoProcess (type) {

    // 파일 업로드



    let newBasicInfo = JSON.parse(localStorage.newBasicInfo);
    newBasicInfo.detailInfo = newBasicInfo.contents;



    // localstorage 비우기
}





function e1Exception() {
    let check = confirm('등록된 교회정보가 없습니다. 신규 등록하시겠습니까?');
    if(check){
        location.href="/admin/basicInfo/1";
    }else{
        location.href="/admin";
    }
}

function e2Exception() {
    alert("등록 중 오류가 발생했습니다");
    location.href="/admin";
}

function createSuccess() {
    alert("성공적으로 등록되었습니다");
    location.href="/admin/basicInfo";
}

function updateSuccess() {
    alert("성공적으로 수정되었습니다");
    location.href="/admin/basicInfo";
}




// ================= 주소 검색 함수 ====================
function addressSearch() {
    new daum.Postcode({
        oncomplete: function(data) {

            var addr = '';

            // 도로명 주소 (R)
            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
                // 지번주소 (J)
            } else {
                addr = data.jibunAddress;
            }
            $("#basicAddress").val(addr);
            $("#detailAddress").focus();
        }
    }).open();
}


function goChapter1(){
    $("#basicInfoFrm2").attr("action", "/admin/basicInfo/create/chapter1/reload");
    $("#basicInfoFrm2").submit();
}


function cancelBtn(){
    var cfm =confirm("정말 취소하시겠습니까")
    if(cfm)
        location.href="/admin";
}

function basicInfoSave() {
    var cfm = confirm("저장하시겠습니까")
    if(cfm)
        $("#basicInfoFrm3").submit();
}

function basicInfoUpdate() {
    var cfm = confirm("수정하시겠습니까")
    if(cfm){
        $("#method").val("PUT");
        $("#basicInfoFrm3").attr('action', "/admin/basicInfo/update").submit();
    }
}