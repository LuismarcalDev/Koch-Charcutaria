// Animações ScrollReveal (cestas renderizadas pelo servidor)
if (typeof ScrollReveal !== 'undefined') {
  ScrollReveal().reveal('.produto', {
    origin: 'bottom',
    duration: 900,
    distance: '40px',
    easing: 'ease-out',
    interval: 150
  });
  ScrollReveal().reveal('.texto-sobre-imagem', {
    origin: 'top',
    duration: 2000,
    distance: '50px',
    easing: 'ease-out'
  });
  ScrollReveal().reveal('.bloco-selos', {
    origin: 'right',
    duration: 800,
    distance: '30px',
    easing: 'ease-out',
    interval: 100
  });
}
