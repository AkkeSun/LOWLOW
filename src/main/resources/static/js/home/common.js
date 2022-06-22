/*!
* Start Bootstrap - Stylish Portfolio v6.0.4 (https://startbootstrap.com/theme/stylish-portfolio)
* Copyright 2013-2021 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-stylish-portfolio/blob/master/LICENSE)
*/
window.addEventListener('DOMContentLoaded', event => {

    const sidebarWrapper = document.getElementById('sidebar-wrapper');
    let scrollToTopVisible = false;
    // Closes the sidebar menu
    const menuToggle = document.body.querySelector('.menu-toggle');
    menuToggle.addEventListener('click', event => {
        event.preventDefault();
        sidebarWrapper.classList.toggle('active');
        _toggleMenuIcon();
        menuToggle.classList.toggle(
            'active');
    })

    // Closes responsive menu when a scroll trigger link is clicked
    var scrollTriggerList = [].slice.call(document.querySelectorAll('#sidebar-wrapper .js-scroll-trigger'));
    scrollTriggerList.map(scrollTrigger => {
        scrollTrigger.addEventListener('click', () => {
            sidebarWrapper.classList.remove('active');
            menuToggle.classList.remove('active');
            _toggleMenuIcon();
        })
    });

    function _toggleMenuIcon() {
        const menuToggleBars = document.body.querySelector('.menu-toggle > .fa-bars');
        const menuToggleTimes = document.body.querySelector('.menu-toggle > .fa-times');
        if (menuToggleBars) {
            menuToggleBars.classList.remove('fa-bars');
            menuToggleBars.classList.add('fa-times');
        }
        if (menuToggleTimes) {
            menuToggleTimes.classList.remove('fa-times');
            menuToggleTimes.classList.add('fa-bars');
        }
    }

    // Scroll to top button appear
    document.addEventListener('scroll', () => {
        const scrollToTop = document.body.querySelector('.scroll-to-top');
        if (document.documentElement.scrollTop > 100) {
            if (!scrollToTopVisible) {
                fadeIn(scrollToTop);
                scrollToTopVisible = true;
            }
        } else {
            if (scrollToTopVisible) {
                fadeOut(scrollToTop);
                scrollToTopVisible = false;
            }
        }
    })
})

function fadeOut(el) {
    el.style.opacity = 1;
    (function fade() {
        if ((el.style.opacity -= .1) < 0) {
            el.style.display = "none";
        } else {
            requestAnimationFrame(fade);
        }
    })();
};

function fadeIn(el, display) {
    el.style.opacity = 0;
    el.style.display = display || "block";
    (function fade() {
        var val = parseFloat(el.style.opacity);
        if (!((val += .1) > 1)) {
            el.style.opacity = val;
            requestAnimationFrame(fade);
        }
    })();
};


//-------------- 카카오맵 로드 ----------------
function goKakaoMap(){
    location.href = "https://map.kakao.com/link/to/위레세움교회,37.46578518126496,127.14047519153812";
}


//-------------- 반응형 함수 ----------------
function activeDeviceFunc(){
    if($(window).width() > 425){
        $("#map").attr("src", "/img/map.png");
        $("#bus2").html("- 버스 2 : 위례역 푸르지오5,6단지 아이페리온 정류장에서 20m")
    }
    else {
        $("#map").attr("src", "/img/map.png");
        $("#bus2").html("- 버스 2 : 위례역 푸르지오5,6단지 아이페리온<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp;정류장에서 20m")
    }
}


// 주소값을 입력하면 위도 경도를 출력하는 함수 (Kakao Local)
function getAddressPath(address){

    var url = "https://dapi.kakao.com/v2/local/search/address.json?query="+address;
    var apiKEy = 'KakaoAK 13313c74ca7bcf1af98bec7853756162'; // KakaoAK 유저 RestAPI Key
    var addressPath = {};

    $.ajax({
        url:url,
        type:'GET',
        headers: {'Authorization' : apiKEy},
        success:function(data){
            addressPath.latitude = data.documents[0].address.y;   // 위도
            addressPath.longitude = data.documents[0].address.x;  // 경도
        },
        error : function(e){
            console.log(e);
        }
    });

    return addressPath;
}