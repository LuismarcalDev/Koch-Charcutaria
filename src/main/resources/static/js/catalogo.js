// ScrollReveal animations (os produtos já são renderizados pelo servidor via Thymeleaf)
ScrollReveal().reveal(".produto", {
  origin: "bottom", duration: 900, distance: "40px",
  easing: "ease-out", interval: 150
});
ScrollReveal().reveal(".sobre .texto", {
  origin: "left", duration: 1000, distance: "5%"
});
ScrollReveal().reveal(".sobre .imagens", {
  origin: "right", duration: 1200, distance: "50px", easing: "ease-out"
});
ScrollReveal().reveal(".texto-sobre-imagem", {
  origin: "top", duration: 2000, distance: "50px", easing: "ease-out"
});
ScrollReveal().reveal(".bloco-selos", {
  origin: "top", duration: 2000, distance: "50px", easing: "ease-out", interval: 200
});

// Menu hamburguer (mobile)
const menuToggle = document.querySelector(".menu-toggle");
const navLinks = document.querySelector(".nav-links");

menuToggle.addEventListener("click", () => {
  navLinks.classList.toggle("show");
  document.body.style.overflow =
    navLinks.classList.contains("show") ? "hidden" : "auto";
});

// Scroll suave com offset para não ficar atrás da navbar
document.querySelectorAll('a[href^="#"]').forEach(link => {
  link.addEventListener('click', function(e) {
    const alvo = document.querySelector(this.getAttribute('href'));
    if (!alvo) return;
    e.preventDefault();
    const offset = 80; // altura da navbar
    const topo = alvo.getBoundingClientRect().top + window.scrollY - offset;
    window.scrollTo({ top: topo, behavior: 'smooth' });
  });
});
