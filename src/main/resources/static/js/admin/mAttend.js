
// ==================== MemberAttend 전역변수 ================
var requestData = {};



// ================= getM_AttendList Data Load  ====================
function getM_AttendList(nowPage){

    requestData.searchId   = "checkDate";
    requestData.searchData = $("#checkDate").val();
    requestData.nowPage    = nowPage;
    requestData.belong     = $("#belong").val();

    // param setting
    var type        = "get";
    var data        = requestData.serialize();
    var url         = "/api/memberAttend";
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var async       = true;
    var callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        var appendData = "";
        if( data.memberAttendList ) {

            var list = data.memberAttendList;

            list.forEach(function (data, index) {

                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${index+1}
                                    </td>
                                    <td style="text-align: left;">
                                         <a href='/admin/memberAttend/${data.checkDate}'>
                                            ${data.checkDate}
                                        </a>
                                    </td>
                                    <td style="text-align: left;">
                                       ${data.name}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.phoneNumber}
                                    </td>
                                    <th style="test-align:center;">
                                    </th>
                                    <th style="test-align:center;">
                                    </th>
                                     <td style="text-align: left;">
                                    </td>
                                </tr>
                                `
            });
        }
        $("tr").remove("#appendItem");       // 기존 데이터 삭제
        $("#appendPath").append(appendData); // data append
    });
};










// ================= getM_AttendCreateTable Data Load  ====================
function getM_AttendCreateTable(nowPage){

    requestData.searchId   = "belong";
    requestData.searchData = $("#belong").val();
    requestData.nowPage    = nowPage;
    requestData.is_MAttend = true;

    // param setting
    var type        = "get";
    var data        = requestData.serialize();
    var url         = "/api/members";
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var async       = true;
    var callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        var appendData = "";
        var idData = "";
        if( data._embedded ) {

            var list = data._embedded.memberList;
            mAttendCreatListCnt = list.length;

            list.forEach(function (data, index) {
                let churchOfficer = churchOfficerChangeToKor(data.churchOfficer);

                idData     += `<input type='hidden' id='${index+1}_id' value='${data.id}'>`
                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${index+1}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.belong}
                                    </td>
                                    <td style="text-align: left;">
                                       ${data.name}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.phoneNumber}
                                    </td>
                                    <td style="text-align: left;">
                                        ${churchOfficer}
                                    </td>
                                    <th style="test-align:center;">
                                        <input type="radio" name="${index+1}_isAttend" value="true">
                                    </th>
                                    <th style="test-align:center;">
                                        <input type="radio" name="${index+1}_isAttend" value="false" checked>
                                    </th>
                                     <td style="text-align: left;">
                                         <input type="text" id="${index+1}_note" value="">
                                    </td>
                                </tr>
                                `
            });
        }
        $("tr").remove("#appendItem");       // 기존 데이터 삭제
        $("#appendPath").append(appendData); // data append
        $("#appendIdPath").append(idData);   // data append

    });
};





function create_mAttendProcess(){
    if(mAttendValidation()){
        create_mAttend();
    }
}




// ================= 출석 등록 유효성 검사 ====================
function mAttendValidation() {

    if($("#checkDate").val() == "") {
        alert('기준 날짜를 선택해주세요');
        return false;
    }
    return true;
}



// ======================== 출석 등록  =====================
function create_mAttend() {
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var url         = "/api/memberAttend";
    var async       = true;
    var type        = 'post';
    var checkNum    = 0;
    var mAttendList = [];

    for(var i=0; i < mAttendCreatListCnt; i++) {
        var memberAttend = {};
        var memberId  = `${i+1}_id`;
        var isAttend  = `${i+1}_isAttend`;
        var note      = `${i+1}_note`;

        memberAttend.memberId  = $("#"+memberId).val();
        memberAttend.isAttend  = $(`input[name=${isAttend}]:checked`).val();
        memberAttend.memberId  = $("#"+memberId).val();
        memberAttend.note      = $("#"+note).val();
        memberAttend.checkDate = $("#checkDate").val();

        var callback = ajaxComm(type, JSON.stringify(memberAttend), url, async, csrfHeader, csrfToken);
        callback.done( function(data){
            console.log(data);
        //    mAttendList.push(data);
        });
    }
}


// ================= Member Update View Setting ====================
function memberUpdateViewSetting() {
    $("#name").removeAttr("disabled");
    $("#phoneNumber").removeAttr("disabled");
    $("#gender").removeAttr("disabled");
    $("#birthDay").removeAttr("disabled");
    $("#belong").removeAttr("disabled");
    $("#name").removeAttr("disabled");
    $("#regiDate").removeAttr("disabled");
    $("#churchOfficer").removeAttr("disabled");
    $("#datePicker").removeAttr("disabled");
    $("#image").removeAttr("disabled");
};

