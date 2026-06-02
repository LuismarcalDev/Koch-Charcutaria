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

// Menu mobile e scroll de âncoras: site-nav.js
