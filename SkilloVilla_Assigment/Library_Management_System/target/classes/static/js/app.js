window.addEventListener('DOMContentLoaded', () => {
    const navButtons = document.querySelectorAll('.nav-btn');
    const views = document.querySelectorAll('.view');

    navButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const target = btn.dataset.view; // home / books / readers / admin

            // перемикаємо активну кнопку
            navButtons.forEach(b => b.classList.toggle('active', b === btn));

            // показуємо потрібний екран
            views.forEach(view => {
                view.classList.toggle('view--active', view.id === `view-${target}`);
            });
        });
    });
});
