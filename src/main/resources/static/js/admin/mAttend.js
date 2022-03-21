

// ================= getM_AttendList Data Load  ====================
function getM_AttendList(nowPage){

    // param setting
    var type        = "get";
    var data        = `searchId=checkDate&searchData=${$("#checkDate").val()}&nowPage=${nowPage}&belong=&${$("#belong").val()}`;
    var url         = "/api/memberAttend";
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var async       = false;
    var callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        var appendData = "";
        if( data.memberAttendList ) {
            totalPages = data.page.totalPages;

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
                                       ${data.isAttendTrue}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.isAttendFalse}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.total}
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

    // param setting
    var url         = '/api/members'
    var type        = "get";
    var data        = `searchId=belong&searchData=${$("#belong").val()}&nowPage=${nowPage}&is_MAttend=true`;
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






// ================= getM_AttendUpdateTable Data Load  ====================
function getM_AttendUpdateTable(){

    // param setting
    var url         = `/api/memberAttend/${$('#checkDate').val()}`
    var type        = "get";
    var data        = `belong=${$('#belong').val()}`;
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var async       = true;
    var callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        var appendData = "";
        var idData = "";

        console.log(data);

        if( data.memberAttendList ) {

            var list = data.memberAttendList;
            mAttendCreatListCnt = list.length;

            list.forEach(function (data, index) {
                var churchOfficer = churchOfficerChangeToKor(data.member.churchOfficer);
                var isAttendData =  getIsAttendData(`${index+1}_isAttend`, data.attend);

                idData     += `<input type='hidden' id='${index+1}_id' value='${data.member.id}'>`
                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${index+1}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.member.belong}
                                    </td>
                                    <td style="text-align: left;">
                                       ${data.member.name}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.member.phoneNumber}
                                    </td>
                                    <td style="text-align: left;">
                                        ${churchOfficer}
                                    </td>
                                    ${isAttendData}
                                     <td style="text-align: left;">
                                         <input type="text" id= "${index+1}_note" value="${data.note}">
                                    </td>
                                </tr>
                                `
            });
        }
        $("tr").remove("#appendItem");  // 기존 데이터 삭제
        $("#appendPath").append(appendData);   // data append
        $("#appendIdPath").append(idData);     // data append

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

        ajaxComm(type, JSON.stringify(memberAttend), url, async, csrfHeader, csrfToken);
    }

    alert('성공적으로 등록되었습니다');
    location.href = '/admin/memberAttend';
}





// ========== isAttend 처리 ===============
function getIsAttendData(name, data){
    var returnData = '';

    if(data == true) {
        returnData = `<th style="test-align:center;">
                        <input type="radio" name="${name}" value="true" checked>
                      </th>
                      <th style="test-align:center;">
                        <input type="radio" name="${name}" value="false">
                      </th>`
    } else {
        returnData = `<th style="test-align:center;">
                        <input type="radio" name="${name}" value="true">
                      </th>
                      <th style="test-align:center;">
                        <input type="radio" name="${name}" value="false" checked>
                      </th>`
    }

    return returnData;
}