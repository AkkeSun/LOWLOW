function calendarSetting(){

    // param setting
    var type        = "get";
    var data        = "";
    var url         = REST_API_URL+"/calendars";
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var async       = false;
    var callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done(function (data) {


        // =============== append Data Setting =============
        var event = [];
        $.each(data, function (i, entry) {
            event.push({
                id     : entry.id,
                title  : entry.title,
                start  : entry.start,
                end    : entry.end,
                allDay : true
            });
        });


        //================ calendar basic setting ===============
        var calendarEl = $("#calendar")[0];
        var calendar = new FullCalendar.Calendar(calendarEl, {

            // 툴바 설정
            headerToolbar: {
                left: "prev,next,today",
                center: "title",
                right: 'dayGridMonth,listMonth'
            },

            initialDate: new Date(),  // 처음 실행 시 기준되는 날짜 ('YYYY-mm-DD')
            locale: 'ko',             // 언어 설정
            navLinks: true,           // 오른쪽 링크 사용유무
            editable: true,           // 수정 가능유무
            nowIndicator: true,       // 현재시간 마크
            selectable: true,         // 달력일자 드래그 설정가능
            dayMaxEvents: true,       // 이벤트가 오버되면 높이 제한 (+몇개로 표현)
            events: event,            // data append
            buttonText: {
                today: '오늘',
                month: '달력',
                list: '주간일정표'
            },


            //================ calendar create modal ===============
            //날짜를 드래그(클릭)하면 실행되는 이벤트
            select: function (arg) {

                var day = `${arg.startStr} ~ ${dateFormatForCal(arg.end)}`
                $("#start").val(arg.startStr);
                $("#end").val(arg.endStr);
                $("#modalTitle").text(`일정 등록 (${day})`)
                
                $("#title").show();
                $("#createProcessBtn").show();
                $("#appendTitle").hide();
                $("#deleteBtn").hide();
                $("#updateViewBtn").hide();
                $("#updateProcessBtn").hide();

                $("#calendarModal").modal('show');
            },


            //================ 일정 기간을 드래그하여 수정 ===============
            //캘린더 데이터가 변경되면 실행되는 이벤트
            eventChange: function(arg) {
                console.log('[calendar update]');
                console.log(arg.event.endStr);

                $('#id').val(arg.event.id);
                $("#end").val(arg.event.endStr);
                $("#start").val(arg.event.startStr);
                $("#title").val(arg.event.title);
                commonInsertAndUpdate('calendars', 'put');
            },

        });

        // rendering
        calendar.render();



        //================ UPDATE & DELETE 모달 오픈 ===============
        //일정을 눌렀을 때 실행되는 이벤트
        calendar.on("eventClick", function (arg) {

            var day = `${arg.event.startStr} ~ ${dateFormatForCal(arg.event.end)}`
            $('#id').val(arg.event.id);
            $("#end").val(arg.event.endStr);
            $("#start").val(arg.event.startStr);
            $("#title").val(arg.event.title);
            $("#modalTitle").text(`${day} 일정`);
            $("#appendTitle").text(arg.event.title);


            $("#appendTitle").show();
            $("#deleteBtn").show();
            $("#updateViewBtn").show();
            $("#title").hide();
            $("#createProcessBtn").hide();
            $("#updateProcessBtn").hide();

            $("#calendarModal").modal('show');
        });
    });
}



//================ 수정버튼 클릭시 ===============
function calendarUpdateViewSetting(){
    $("#title").show();
    $("#appendTitle").hide();
}



//================ 캘린더 endDate format ===============
function dateFormatForCal(userDay){
    var year = userDay.getFullYear();
    var month = userDay.getMonth() + 1;
    var date = userDay.getDate() - 1;
    return `${year}-${month >= 10 ? month : '0' + month}-${date >= 10 ? date : '0' + date}`;
}

