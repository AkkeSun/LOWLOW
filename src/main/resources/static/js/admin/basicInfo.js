
function goBasicInfo() {

    let type        = "get";
    let data        = "";
    let url         = REST_API_URL + "/basicInfo/list";
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let async       = false;
    let callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( data => {
        if(data.basicInfoList.length ==0) {
            e1Exception();
        } else {
            localStorage.setItem('basicInfo', JSON.stringify(data.basicInfoList[0]));
            localStorage.setItem('isUpdate', 'true');
            location.href = "/admin/basicInfo";
        }
    })
}


function setBasicInfoView1 () {

    let isUpdate = localStorage.isUpdate
    let basicInfoData = JSON.parse(localStorage.basicInfo);

    if (isUpdate == 'true') {
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


function e1Exception() {
    let check = confirm('등록된 교회정보가 없습니다. 신규 등록하시겠습니까?');
    if(check){
        location.href="/admin/basicInfo/create/chapter1";
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

function goView2(){

    if(view1Validation ()) {
        alert('success');
        // 데이터 저장 후 이동
    }

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