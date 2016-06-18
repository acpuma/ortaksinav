$(function() {
   $("#toggleMenu").on('click',function () {
       $(".wrapmenu").toggleClass('menuhide');
       $(".wrapcontent").toggleClass('contentfull');
   })
});