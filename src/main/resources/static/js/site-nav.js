document.addEventListener('DOMContentLoaded', function () {
  var navbar = document.getElementById('siteNavbar');
  if (!navbar) return;

  var menuToggle = navbar.querySelector('.menu-toggle');
  var navLinks = navbar.querySelector('.nav-links');
  if (!menuToggle || !navLinks) return;

  function fecharMenu() {
    navLinks.classList.remove('show');
    menuToggle.setAttribute('aria-expanded', 'false');
    document.body.style.overflow = '';
  }

  menuToggle.addEventListener('click', function () {
    var open = navLinks.classList.toggle('show');
    menuToggle.setAttribute('aria-expanded', open ? 'true' : 'false');
    document.body.style.overflow = open ? 'hidden' : '';
  });

  navLinks.querySelectorAll('a').forEach(function (link) {
    link.addEventListener('click', fecharMenu);
  });

  document.querySelectorAll('a[href^="#"]').forEach(function (link) {
    link.addEventListener('click', function (e) {
      var href = link.getAttribute('href');
      if (!href || href.length < 2) return;
      var alvo = document.querySelector(href);
      if (!alvo) return;
      e.preventDefault();
      var offset = 90;
      var topo = alvo.getBoundingClientRect().top + window.scrollY - offset;
      window.scrollTo({ top: topo, behavior: 'smooth' });
    });
  });
});
