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