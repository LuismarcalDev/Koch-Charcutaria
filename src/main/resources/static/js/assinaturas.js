const produtos = [
  {
    nome: "Bronze",
    imagem: "/Imagens/img1catalogo.jpeg",
    preco: "R$ 79,99",
    descricao: "A porta de entrada para o mundo Koch. Todo mês você recebe em casa 2 itens de charcutaria artesanal — como panceta defumada ou linguiça toscana — mais 2 mimos cuidadosamente selecionados, como geleia gourmet e biscoitos artesanais. Ideal para quem quer descobrir sabores únicos sem abrir mão do bolso."
  },
  {
    nome: "Prata",
    imagem: "/Imagens/img2catalogo.jpeg",
    preco: "R$ 129,99",
    descricao: "Um passo acima no prazer à mesa. A Cesta Prata traz mensalmente 3 itens de charcutaria artesanal — incluindo cortes como copa defumada e pancetta arrotolata — acompanhados de 4 mimos especiais, como azeite extravirgem, queijo artesanal, patê de ervas e mel silvestre. Para quem aprecia o melhor do sabor com mais variedade."
  },
  {
    nome: "Ouro",
    imagem: "/Imagens/img3catalogo.jpeg",
    preco: "R$ 189,99",
    descricao: "A experiência completa Koch na sua porta. São 5 itens de charcutaria premium — lombos, copas, pancetas e embutidos exclusivos — mais 5 mimos selecionados a dedo: cervejas artesanais, geleias especiais, queijos curados, crackers gourmet e azeite aromatizado. Para quem vive intensamente cada refeição."
  },
  {
    nome: "Diamante",
    imagem: "/Imagens/img4catalogo.jpeg",
    preco: "R$ 259,99",
    descricao: "Exclusividade e abundância em cada entrega. A Cesta Diamante reúne 7 itens de charcutaria artesanal dos cortes mais nobres — incluindo edições especiais e peças de cura longa — junto de 7 mimos premium: vinhos selecionados, antepastos finos, queijos importados, geleias de frutas raras, azeite trufado, castanhas temperadas e muito mais."
  },
  {
    nome: "Esmeralda",
    imagem: "/Imagens/img5catalogo.jpeg",
    preco: "R$ 349,99",
    descricao: "O topo da linha Koch, para quem não abre mão do extraordinário. Todo mês chegam 10 itens de charcutaria artesanal de altíssimo padrão, com peças exclusivas e sazonais, acompanhados de 10 mimos harmonizados: espumantes, queijos premiados, antepastos importados, azeites especiais, geleias artesanais, nuts gourmet, chocolates finos e surpresas únicas a cada entrega."
  }
];

const container = document.getElementById("produtos-container");

produtos.forEach(produto => {
  const produtoDiv = document.createElement("div");
  produtoDiv.classList.add("produto");

  const descricaoDiv = document.createElement("div");
  descricaoDiv.classList.add("descricao");

  const fotoCard = document.createElement("div");
  fotoCard.classList.add("foto-card");
  fotoCard.innerHTML = `
    <img class="produto-foto" src="${produto.imagem}" alt="${produto.nome}">
    <div class="foto-card-preco">${produto.preco}</div>
    <button class="foto-card-btn">Assinar 🍽️</button>
  `;

  const descricaoTexto = document.createElement("div");
  descricaoTexto.classList.add("descricao-texto");
  descricaoTexto.innerHTML = `
    <span class="subtitulo-cesta">Cesta</span>
    <strong class="produto-nome">${produto.nome}</strong>
    <p>${produto.descricao}</p>
  `;

  descricaoDiv.appendChild(fotoCard);
  descricaoDiv.appendChild(descricaoTexto);

  const selosDiv = document.createElement("div");
  selosDiv.classList.add("bloco-selos");
  selosDiv.innerHTML = `
    <img src="/Imagens/selo1.png" alt="Selo 1">
    <img src="/Imagens/selo2.png" alt="Selo 2">
    <img src="/Imagens/selo3.png" alt="Selo 3">
    <img src="/Imagens/selo4.png" alt="Selo 4">
    <img src="/Imagens/selo5.png" alt="Selo 5">
  `;

  produtoDiv.appendChild(descricaoDiv);
  produtoDiv.appendChild(selosDiv);
  container.appendChild(produtoDiv);
});

// Animações ScrollReveal
ScrollReveal().reveal(".produto", {
  origin: "bottom", duration: 900, distance: "40px",
  easing: "ease-out", interval: 150
});
ScrollReveal().reveal(".texto-sobre-imagem", {
  origin: "top", duration: 2000, distance: "50px", easing: "ease-out"
});
ScrollReveal().reveal(".bloco-selos", {
  origin: "right", duration: 800, distance: "30px", easing: "ease-out", interval: 100
});

// Menu hamburguer
const menuToggle = document.querySelector(".menu-toggle");
const navLinks = document.querySelector(".nav-links");

menuToggle.addEventListener("click", () => {
  navLinks.classList.toggle("show");
  document.body.style.overflow =
    navLinks.classList.contains("show") ? "hidden" : "auto";
});