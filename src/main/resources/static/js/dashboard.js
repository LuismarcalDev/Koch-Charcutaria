function switchTab(name, clickedEl) {
      document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
      document.querySelectorAll('.sidebar-item').forEach(el => el.classList.remove('active'));
      document.getElementById('tab-' + name).classList.add('active');
      clickedEl.classList.add('active');
    }

function atualizarData() {
    const agora = new Date();

    const dataFormatada = agora.toLocaleDateString('pt-BR', {
        weekday: 'long',
        day: 'numeric',
        month: 'long'
    });

    document.getElementById("data").textContent =
        dataFormatada.charAt(0).toUpperCase() + dataFormatada.slice(1);
}

atualizarData();

function abrirModal() {
    document.getElementById('modal-titulo').innerText = 'Novo Produto';
    document.getElementById('produto-id').value = '';
    document.getElementById('produto-nome').value = '';
    document.getElementById('produto-descricao').value = '';
    document.getElementById('produto-imagemUrl').value = '';
    document.getElementById('produto-quantidade').value = '';
    document.getElementById('produto-valor').value = '';
    document.getElementById('modal-produto').style.display = 'flex';
}

function editarProduto(id, nome, descricao, imagemUrl, quantidade, valor) {
    document.getElementById('modal-titulo').innerText = 'Editar Produto';
    document.getElementById('produto-id').value = id;
    document.getElementById('produto-nome').value = nome;
    document.getElementById('produto-descricao').value = descricao;
    document.getElementById('produto-imagemUrl').value = imagemUrl;
    document.getElementById('produto-quantidade').value = quantidade;
    document.getElementById('produto-valor').value = valor;
    document.getElementById('modal-produto').style.display = 'flex';
}

function fecharModal() {
    document.getElementById('modal-produto').style.display = 'none';
}