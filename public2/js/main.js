$(document).ready(function () {

    //반응형 초기화
    winH = 0
    winW = 0
    var goTop = $(".goTopBt");
    var delay = 500;

    //높이,너비를 윈도우의 창의 높이로 설정
    winH = $(window).innerHeight();
    winW = $(window).innerWidth();
    winResize(); //초기값설정



    //반응형 자동크기 설정
    $(window).resize(function () {
        winResize();
    });
    
    function winResize() {
        winH = $(window).innerHeight();
        winW = $(window).innerWidth();
    }

    //스크롤 발생하면 goTopBt 보이기
        goTop.hide();
        
        $(window).scroll(function () {
        if ($(this).scrollTop() > 200) {
        goTop.fadeIn();
        } else {
            goTop.fadeOut();
        }
        });

    //goTopBt 클릭시 상단으로 이동
        goTop.on("click", function () {
        $("html, body").stop().animate({ scrollTop: 0 }, delay);
        return false;
        });

    // 메뉴 네비게이션 해당 메뉴에서 style
        var CurrentFileName = document.URL.substring(
            document.URL.lastIndexOf("/") + 1,
            document.URL.lastIndexOf("/") + 30
        );
        
    
        if (CurrentFileName.startsWith("faq")) {
            $('#header ul li a').removeClass('on');
            $('#header ul li:nth-child(4) a').addClass('on');  
        }
        if (CurrentFileName.startsWith("notification")) {
            $('#header ul li a').removeClass('on');
            $('#header ul li:nth-child(5) a').addClass('on');
            
        }
        
    // 가입하기 btn js
         $("#header .join_btn").mouseover(function(){
            $(this).addClass("on");
        });
        $("#header .join_btn").mouseleave(function(){
            $(this).removeClass("on");
        });
        $("#main_visual a").mouseover(function(){
            $(this).addClass("on");
        });
        $("#main_visual a").mouseleave(function(){
            $(this).removeClass("on");
        });


	// 통신사 선택 
	   $('#service_join_wrap .service_j_btn').on('click', function(e){
	       $('#service_join_wrap .service_j_btn').removeClass('selected');
	       $(this).addClass('selected');
	   });

    // 모바일 헤더 js
        $('.m_gnb_bg').hide();
        $('.m_gnb_btn').on("click", function () {
            $('.m_gnb_bg').show();
            $('.m_gnb_wrap').animate({
                left:20+'%'
            });
            const body = document.getElementsByTagName('body')[0];
            body.classList.add('scrollLock');
        });
        $('.gnb_close').on("click", function () {
            $('.m_gnb_bg').hide();
            $('.m_gnb_wrap').animate({
                left:100+'%'
            });
            const body = document.getElementsByTagName('body')[0];
            body.classList.remove('scrollLock');
        });
        $('.m_gnb li').on("click", function(){
            $('.m_gnb_bg').hide();
            $('.m_gnb_wrap').animate({
                left:100+'%'
            });
            const body = document.getElementsByTagName('body')[0];
            body.classList.remove('scrollLock');
        });

    //이용혜택
        $("section03_bg").on("click",function(){
            $("#shNotice").toggleClass("open");
          });


    //문의
    $("#section05 ul li .que").click(function() {
        $(this).next("#section05 ul li .anw").stop().slideToggle(300);
       $(this).toggleClass('on').siblings().removeClass('on');
       $(this).next("#section05 ul li .anw").siblings("#section05 ul li .anw").slideUp(300); // 1개씩 펼치기
     });

  
    // 이용혜택 슬릭슬라이드
    $('.single-item').slick({
        dots: true,
        infinite: true, //양방향 무한 모션
        speed:300, // 슬라이드 스피드
        prevArrow : `<img src="img/btn_arrow_left.png" class="prevArrow" alt="<<">`,
        nextArrow : `<img src="img/btn_arrow_right.png" class="nextArrow" alt=">>">`,
        responsive: [
            {
                breakpoint: 645,
            },
            {
                breakpoint: 480,
            },
        ]
    });


});//style.js
