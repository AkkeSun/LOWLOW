//==================== for basicInfoAlert ======================
function e1Exception() {
    var check = confirm('등록된 교회정보가 없습니다. 신규 등록하시겠습니까?');
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

function goChapter2(){
    var cfm = confirm("업로드한 이미지가 삭제됩니다. 계속하시겠습니까")
    if(cfm){
        $("#basicInfoFrm3").attr("action", "/admin/basicInfo/create/chapter2/reload");
        $("#basicInfoFrm3").submit();
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