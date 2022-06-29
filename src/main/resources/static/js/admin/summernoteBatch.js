function summernoteBatch () {

    let masterData  = {};
    let async       = false;
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');

    // data setting
    let callback = ajaxComm("get", "", `${REST_API_URL}/summerNote`, async, csrfHeader, csrfToken);
    callback.done( data => {
        masterData.summernoteImgList = data.summernoteImgList
    })
    callback = ajaxComm("get", "", `${REST_API_URL}/galleries/list`, async, csrfHeader, csrfToken);
    callback.done( data => {
        masterData.galleryList = data.galleryList
    })
    callback = ajaxComm("get", "", `${REST_API_URL}/notices/list`, async, csrfHeader, csrfToken);
    callback.done( data => {
        masterData.noticeList = data.noticeList
    })

    // batch process start
    callback = ajaxComm("post", JSON.stringify(masterData), "/batch/summernote", async, csrfHeader, csrfToken);
    callback.done(() => {});
}

