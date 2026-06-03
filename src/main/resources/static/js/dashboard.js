function switchTab(name, clickedEl) {
    document.querySelectorAll('.tab-content').forEach(function (el) {
        el.classList.remove('active');
    });
    document.querySelectorAll('.sidebar-item').forEach(function (el) {
        el.classList.remove('active');
    });
    var tabEl = document.getElementById('tab-' + name);
    if (tabEl) {
        tabEl.classList.add('active');
    }
    if (clickedEl) {
        clickedEl.classList.add('active');
    }
}

function abrirTabInicial() {
    var params = new URLSearchParams(window.location.search);
    var tab = params.get('tab');
    if (!tab) {
        return;
    }
    var sidebarItem = document.querySelector('.sidebar-item[data-tab="' + tab + '"]');
    switchTab(tab, sidebarItem);
}

function atualizarData() {
    var el = document.getElementById('data');
    if (!el) {
        return;
    }
    var agora = new Date();
    var dataFormatada = agora.toLocaleDateString('pt-BR', {
        weekday: 'long',
        day: 'numeric',
        month: 'long'
    });
    el.textContent = dataFormatada.charAt(0).toUpperCase() + dataFormatada.slice(1);
}

function configurarCampoId(modoEdicao) {
    var idField = document.getElementById('produto-id');
    if (!idField) {
        return;
    }
    if (modoEdicao) {
        idField.setAttribute('name', 'id');
    } else {
        idField.value = '';
        idField.removeAttribute('name');
    }
}

function configurarImagemObrigatoria(obrigatoria) {
    var input = document.getElementById('produto-imagemArquivo');
    if (!input) {
        return;
    }
    if (obrigatoria) {
        input.setAttribute('required', 'required');
    } else {
        input.removeAttribute('required');
    }
}

function abrirModal() {
    document.getElementById('modal-titulo').innerText = 'Nova cesta';
    document.getElementById('produto-nome').value = '';
    document.getElementById('produto-descricao').value = '';
    document.getElementById('produto-imagemArquivo').value = '';
    document.getElementById('produto-preco').value = '';
    configurarCampoId(false);
    configurarImagemObrigatoria(true);
    var imgAtual = document.getElementById('produto-imagem-atual');
    imgAtual.textContent = '';
    imgAtual.style.display = 'none';
    document.getElementById('modal-produto').style.display = 'flex';
    document.body.classList.add('modal-aberto');
}

function editarProduto(btn) {
    document.getElementById('modal-titulo').innerText = 'Editar cesta';
    var idField = document.getElementById('produto-id');
    idField.value = btn.getAttribute('data-id') || '';
    configurarCampoId(true);
    configurarImagemObrigatoria(false);
    document.getElementById('produto-nome').value = btn.getAttribute('data-nome') || '';
    document.getElementById('produto-descricao').value = btn.getAttribute('data-descricao') || '';
    document.getElementById('produto-imagemArquivo').value = '';
    var imgAtual = document.getElementById('produto-imagem-atual');
    var imagem = btn.getAttribute('data-imagem');
    if (imagem) {
        imgAtual.textContent = 'Imagem atual: ' + imagem;
        imgAtual.style.display = 'block';
    } else {
        imgAtual.textContent = '';
        imgAtual.style.display = 'none';
    }
    document.getElementById('produto-preco').value = btn.getAttribute('data-preco') || '';
    document.getElementById('modal-produto').style.display = 'flex';
    document.body.classList.add('modal-aberto');
}

function fecharModal() {
    document.getElementById('modal-produto').style.display = 'none';
    document.body.classList.remove('modal-aberto');
}

function initDashboard() {
    abrirTabInicial();
    atualizarData();

    var modal = document.getElementById('modal-produto');
    if (modal) {
        modal.addEventListener('click', function (e) {
            if (e.target === modal) {
                fecharModal();
            }
        });
    }

    var form = document.getElementById('form-produto');
    if (form) {
        form.addEventListener('submit', function () {
            var idField = document.getElementById('produto-id');
            if (idField && !idField.getAttribute('name')) {
                idField.removeAttribute('value');
            }
        });
    }

    if (document.body.getAttribute('data-abrir-modal') === 'true') {
        switchTab('criar', document.querySelector('.sidebar-item[data-tab="criar"]'));
        abrirModal();
    }
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initDashboard);
} else {
    initDashboard();
}
