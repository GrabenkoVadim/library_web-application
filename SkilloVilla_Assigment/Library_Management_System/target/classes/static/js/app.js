// Поточний користувач
let currentUser = {
    username: null,
    role: null
};

function showToast(message, type = 'info', timeout = 3000) {
    const container = document.getElementById('toast-container');
    if (!container) {
        alert(message); // fallback, якщо раптом немає контейнера
        return;
    }

    const div = document.createElement('div');
    div.classList.add('toast', `toast--${type}`);
    div.innerHTML = `
        <span>${message}</span>
        <button class="toast__close" type="button">&times;</button>
    `;

    container.appendChild(div);

    // показати з анімацією
    requestAnimationFrame(() => {
        div.classList.add('toast--show');
    });

    // закриття по кліку на хрестик
    const closeBtn = div.querySelector('.toast__close');
    if (closeBtn) {
        closeBtn.addEventListener('click', () => {
            hideToast(div);
        });
    }

    // авто-закриття
    if (timeout && timeout > 0) {
        setTimeout(() => hideToast(div), timeout);
    }
}

function hideToast(toastEl) {
    toastEl.classList.remove('toast--show');
    setTimeout(() => {
        if (toastEl.parentNode) {
            toastEl.parentNode.removeChild(toastEl);
        }
    }, 200); // співпадає з transition
}

let currentBooks = [];
let currentReaders = [];
let adminUsers = [];

// ---------- ФІЛЬТР КНИГ ----------
function applyBooksFilter() {
    const searchInput = document.getElementById('book-search');
    const onlyAvailableCheckbox = document.getElementById('books-only-available');

    const query = searchInput ? searchInput.value.trim() : '';
    const onlyAvailable = !!(onlyAvailableCheckbox && onlyAvailableCheckbox.checked);

    loadBooks({
        query: query || null,
        onlyAvailable
    });
}

// ---------- DOMContentLoaded ----------
window.addEventListener('DOMContentLoaded', async () => {
    const navButtons = document.querySelectorAll('.nav-btn');
    const views = document.querySelectorAll('.view');

    const changePasswordChk = document.getElementById("user-change-password");
    const passwordGroup = document.getElementById("password-group");
    const passwordInput = document.getElementById("user-password");
    const togglePasswordBtn = document.getElementById("toggle-password");

    changePasswordChk.addEventListener("change", () => {
        if (changePasswordChk.checked) {
            passwordGroup.classList.remove("hidden");
            passwordInput.disabled = false;
        } else {
            passwordGroup.classList.add("hidden");
            passwordInput.value = "";
            passwordInput.disabled = true;
        }
    });

    togglePasswordBtn.addEventListener("click", () => {
        passwordInput.type =
            passwordInput.type === "password" ? "text" : "password";
    });

    // перемикання вкладок
    navButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const target = btn.dataset.view;

            // захист адмінки по ролі (працює тільки якщо роль уже відома)
            if (target === 'admin' && currentUser.role && currentUser.role !== 'ADMIN') {
                showToast('У вас немає прав доступу до адмінки.', 'error');
                return;
            }

            navButtons.forEach(b => b.classList.toggle('active', b === btn));
            views.forEach(view => {
                view.classList.toggle('view--active', view.id === `view-${target}`);
            });
        });
    });

    // ---------- КНИГИ ----------
    const addBtn = document.getElementById('book-add-btn');
    if (addBtn) {
        addBtn.addEventListener('click', () => openBookModal());
    }

    const form = document.getElementById('book-form');
    if (form) {
        form.addEventListener('submit', onBookFormSubmit);
    }

    const cancelBtn = document.getElementById('book-cancel-btn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', closeBookModal);
    }

    const tableBody = document.getElementById('books-table-body');
    if (tableBody) {
        tableBody.addEventListener('click', onBooksTableClick);
    }

    // ---------- ЧИТАЧІ ----------
    const readerAddBtn = document.getElementById('reader-add-btn');
    if (readerAddBtn) {
        readerAddBtn.addEventListener('click', () => openReaderModal());
    }

    const readerForm = document.getElementById('reader-form');
    if (readerForm) {
        readerForm.addEventListener('submit', onReaderFormSubmit);
    }

    const readerCancelBtn = document.getElementById('reader-cancel-btn');
    if (readerCancelBtn) {
        readerCancelBtn.addEventListener('click', closeReaderModal);
    }

    const readersTableBody = document.getElementById('readers-table-body');
    if (readersTableBody) {
        readersTableBody.addEventListener('click', onReadersTableClick);
    }

    // ---------- ФІЛЬТРИ КНИГ ----------
    const filterBtn = document.getElementById('book-filter-btn');
    const searchInput = document.getElementById('book-search');
    const onlyAvailableCheckbox = document.getElementById('books-only-available');

    if (filterBtn) {
        filterBtn.addEventListener('click', (e) => {
            e.preventDefault();
            applyBooksFilter();
        });
    }

    if (searchInput) {
        searchInput.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                applyBooksFilter();
            }
        });
    }

    if (onlyAvailableCheckbox) {
        onlyAvailableCheckbox.addEventListener('change', () => {
            applyBooksFilter();
        });
    }

    // ---------- ВИДАЧА КНИГ ----------
    const issueForm = document.getElementById('issue-form');
    if (issueForm) {
        issueForm.addEventListener('submit', onIssueFormSubmit);
    }

    const issueCancelBtn = document.getElementById('issue-cancel-btn');
    if (issueCancelBtn) {
        issueCancelBtn.addEventListener('click', closeIssueModal);
    }

    const bookDetailsCloseBtn = document.getElementById('book-details-close-btn');
    if (bookDetailsCloseBtn) {
        bookDetailsCloseBtn.addEventListener('click', closeBookDetailsModal);
    }

    const bookDetailsModal = document.getElementById('book-details-modal');
    if (bookDetailsModal) {
        const backdrop = bookDetailsModal.querySelector('.modal__backdrop');
        if (backdrop) {
            backdrop.addEventListener('click', closeBookDetailsModal);
        }
    }

    // ---------- АДМІНКА: КОРИСТУВАЧІ ----------
    const usersTableBody = document.getElementById('admin-users-table-body');
    if (usersTableBody) {
        usersTableBody.addEventListener('click', onUsersTableClick);
    }

    const addUserBtn = document.getElementById('admin-add-user-btn');
    if (addUserBtn) {
        addUserBtn.addEventListener('click', () => openUserModal(null));
    }

    const userForm = document.getElementById('user-form');
    if (userForm) {
        userForm.addEventListener('submit', onUserFormSubmit);
    }

    const userCancelBtn = document.getElementById('user-cancel-btn');
    if (userCancelBtn) {
        userCancelBtn.addEventListener('click', closeUserModal);
    }

    const userModal = document.getElementById('user-modal');
    if (userModal) {
        const backdrop = userModal.querySelector('.modal__backdrop');
        if (backdrop) {
            backdrop.addEventListener('click', closeUserModal);
        }
    }

    // ---------- ПЕРШЕ ЗАВАНТАЖЕННЯ ДАНИХ ----------
    // спочатку дізнаємося, хто залогінений
    await initAuthUi();

    loadBooks();
    loadReaders();
    loadRecentOperations();
    loadUsers();

    setInterval(loadRecentOperations, 10000);

});


// ================== КНИГИ ==================

function openBookModal(book) {
    const modal = document.getElementById('book-modal');
    if (!modal) return;

    const idInput = document.getElementById('book-id');
    const nameInput = document.getElementById('book-name');
    const authorInput = document.getElementById('book-author');
    const publisherInput = document.getElementById('book-publisher');
    const descriptionInput = document.getElementById('book-description');

    if (!idInput || !nameInput || !authorInput || !publisherInput || !descriptionInput) {
        console.error('Не знайдені поля форми книги');
        return;
    }

    if (book) {
        idInput.value = book.id;
        nameInput.value = book.name ?? '';
        authorInput.value = book.author ?? '';
        publisherInput.value = book.publisher ?? '';
        descriptionInput.value = book.description ?? '';
    } else {
        idInput.value = '';
        nameInput.value = '';
        authorInput.value = '';
        publisherInput.value = '';
        descriptionInput.value = '';
    }

    modal.classList.remove('hidden');
}

async function loadBooks(options = {}) {
    const { query, onlyAvailable } = options;

    const tbody = document.getElementById('books-table-body');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="6">Завантаження...</td></tr>';

    try {
        const params = new URLSearchParams();

        if (query && query.trim()) {
            params.append('name', query.trim());
            // за бажанням: params.append('author', query.trim());
        }

        if (onlyAvailable === true) {
            params.append('issued', 'false');
        }

        const url = params.toString()
            ? `/api/books?${params.toString()}`
            : `/api/books`;

        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('HTTP ' + response.status);
        }

        const books = await response.json();
        currentBooks = books;
        renderBooks(currentBooks);
    } catch (err) {
        console.error('Помилка при завантаженні книг', err);
        tbody.innerHTML = '<tr><td colspan="6">Не вдалося завантажити книги</td></tr>';
    }
}

function renderBooks(books) {
    const tbody = document.getElementById('books-table-body');
    if (!tbody) return;

    tbody.innerHTML = '';

    if (!books || books.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6">Книг не знайдено</td></tr>';
        return;
    }

    const now = new Date();

    books.forEach(book => {
        const title = book.name ?? '';
        const authorsText = book.author ?? '';
        const year = '';
        const genre = '';

        let isOverdue = false;
        let statusHtml = 'Доступна';
        let statusClass = '';

        if (book.issued) {
            const issue = formatDateTime(book.issueDate);
            const due = formatDateTime(book.dueDate);

            statusHtml = `Видана<br><small>${issue} → ${due}</small>`;

            if (book.dueDate) {
                const dueDate = new Date(book.dueDate);
                if (dueDate < now) {
                    isOverdue = true;
                    statusHtml += `<br><small>(прострочена)</small>`;
                    statusClass = 'status--overdue';
                }
            }
        }

        const actionButtonHtml = book.issued
            ? `<button class="btn btn-warning btn-sm" data-action="return" data-id="${book.id}">Повернути</button>`
            : `<button class="btn btn-light btn-sm" data-action="issue" data-id="${book.id}">Видати</button>`;

        const tr = document.createElement('tr');
        if (isOverdue) {
            tr.classList.add('row--overdue');
        }

        tr.innerHTML = `
            <td>${title}</td>
            <td>${authorsText}</td>
            <td>${year}</td>
            <td>${genre}</td>
            <td class="${statusClass}">${statusHtml}</td>
            <td>
                <button class="btn btn-light btn-sm" data-action="details" data-id="${book.id}">Відкрити</button>
                <button class="btn btn-light btn-sm" data-action="edit" data-id="${book.id}">Редагувати</button>
                ${actionButtonHtml}
                <button class="btn btn-light btn-sm" data-action="delete" data-id="${book.id}">Видалити</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function closeBookModal() {
    const modal = document.getElementById('book-modal');
    if (!modal) return;
    modal.classList.add('hidden');
}

function onBooksTableClick(event) {
    const btn = event.target.closest('button');
    if (!btn) return;

    const action = btn.dataset.action;
    const id = parseInt(btn.dataset.id, 10);
    if (!id) return;

    const book = currentBooks.find(b => b.id === id);
    if (!book) return;

    if (action === 'edit') {
        openBookModal(book);
    } else if (action === 'delete') {
        deleteBook(id);
    } else if (action === 'issue') {
        if (book.issued) {
            showToast('Ця книга вже видана.', 'error');
            return;
        }
        openIssueModal(book);
    } else if (action === 'return') {
        returnBook(book);
    } else if (action === 'details') {
        openBookDetailsModal(book);
    }
}

async function onBookFormSubmit(event) {
    event.preventDefault();

    const id = document.getElementById('book-id').value;
    const name = document.getElementById('book-name').value;
    const author = document.getElementById('book-author').value;
    const publisher = document.getElementById('book-publisher').value;
    const description = document.getElementById('book-description').value;

    const payload = {
        id: id ? parseInt(id, 10) : null,
        name,
        author,
        publisher,
        description
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `/api/books/${id}` : `/api/books`;

    try {
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const text = await response.text();
            throw new Error('HTTP ' + response.status + ': ' + text);
        }

        closeBookModal();
        await loadBooks();
        showToast('Книгу успішно збережено', 'success');
    } catch (err) {
        console.error('Помилка при збереженні книги', err);
        showToast('Не вдалося зберегти книгу.', 'error');
    }
}

async function deleteBook(id) {
    if (!confirm('Видалити цю книгу?')) {
        return;
    }

    try {
        const response = await fetch(`/api/books/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('HTTP ' + response.status);
        }

        await loadBooks();
    } catch (err) {
        console.error('Помилка при видаленні книги', err);
        alert('Не вдалося видалити книгу.');
    }
}

// ================== ЧИТАЧІ ==================

async function loadReaders() {
    try {
        const response = await fetch('/api/readers');
        if (!response.ok) {
            throw new Error('HTTP ' + response.status);
        }
        const readers = await response.json();
        currentReaders = readers;
        renderReaders(currentReaders);
    } catch (err) {
        console.error('Помилка при завантаженні читачів', err);
        currentReaders = [];
        renderReaders(currentReaders);
    }
}

function renderReaders(readers) {
    const tbody = document.getElementById('readers-table-body');
    if (!tbody) return;

    tbody.innerHTML = '';

    if (!readers || readers.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5">Читачів не знайдено</td></tr>';
        return;
    }

    readers.forEach(reader => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${reader.fullName ?? ''}</td>
            <td>${reader.email ?? ''}</td>
            <td>${reader.phone ?? ''}</td>
            <td>${reader.address ?? ''}</td>
            <td>
                <button class="btn btn-light btn-sm" data-r-action="edit" data-id="${reader.id}">Редагувати</button>
                <button class="btn btn-light btn-sm" data-r-action="delete" data-id="${reader.id}">Видалити</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function onReadersTableClick(event) {
    const btn = event.target.closest('button');
    if (!btn) return;

    const action = btn.dataset.rAction;
    const id = parseInt(btn.dataset.id, 10);
    if (!id) return;

    const reader = currentReaders.find(r => r.id === id);

    if (action === 'edit') {
        openReaderModal(reader);
    } else if (action === 'delete') {
        deleteReader(id);
    }
}

function openReaderModal(reader) {
    const modal = document.getElementById('reader-modal');
    if (!modal) return;

    const titleEl = document.getElementById('reader-modal-title');
    const idInput = document.getElementById('reader-id');
    const fullNameInput = document.getElementById('reader-fullName');
    const emailInput = document.getElementById('reader-email');
    const phoneInput = document.getElementById('reader-phone');
    const addressInput = document.getElementById('reader-address');

    if (reader) {
        titleEl.textContent = 'Редагувати читача';
        idInput.value = reader.id;
        fullNameInput.value = reader.fullName ?? '';
        emailInput.value = reader.email ?? '';
        phoneInput.value = reader.phone ?? '';
        addressInput.value = reader.address ?? '';
    } else {
        titleEl.textContent = 'Додати читача';
        idInput.value = '';
        fullNameInput.value = '';
        emailInput.value = '';
        phoneInput.value = '';
        addressInput.value = '';
    }

    modal.classList.remove('hidden');
}

function closeReaderModal() {
    const modal = document.getElementById('reader-modal');
    if (!modal) return;
    modal.classList.add('hidden');
}

async function onReaderFormSubmit(event) {
    event.preventDefault();

    const id = document.getElementById('reader-id').value;
    const fullName = document.getElementById('reader-fullName').value.trim();
    const email = document.getElementById('reader-email').value.trim();
    const phone = document.getElementById('reader-phone').value.trim();
    const address = document.getElementById('reader-address').value.trim();

    const payload = { id: id ? parseInt(id, 10) : null, fullName, email, phone, address };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `/api/readers/${id}` : '/api/readers';

    try {
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const text = await response.text();
            throw new Error('HTTP ' + response.status + ': ' + text);
        }

        closeReaderModal();
        await loadReaders();
        showToast('Читача збережено успішно', 'success');
    } catch (err) {
        console.error('Помилка при збереженні читача', err);
        showToast('Не вдалося зберегти читача.', 'error');
    }
}

async function deleteReader(id) {
    if (!confirm('Видалити цього читача?')) return;

    try {
        const response = await fetch(`/api/readers/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('HTTP ' + response.status);
        }

        await loadReaders();
        await loadBooks();
        showToast('Читача успішно видалено', 'success');
    } catch (err) {
        console.error('Помилка при видаленні читача', err);
        showToast('Не вдалося видалити читача.', 'error');
    }
}

// ================== ВИДАЧА / ПОВЕРНЕННЯ ==================

function openIssueModal(book) {
    const modal = document.getElementById('issue-modal');
    if (!modal) return;

    const bookIdInput = document.getElementById('issue-book-id');
    const bookTitleEl = document.getElementById('issue-book-title');
    const readerSelect = document.getElementById('issue-reader');
    const daysInput = document.getElementById('issue-days');

    if (!bookIdInput || !bookTitleEl || !readerSelect || !daysInput) {
        console.error('Не знайдено один із елементів модалки видачі');
        return;
    }

    bookIdInput.value = book.id;
    bookTitleEl.textContent = book.name ?? '';
    daysInput.value = 14;

    readerSelect.innerHTML = '';

    if (!currentReaders || currentReaders.length === 0) {
        const opt = document.createElement('option');
        opt.value = '';
        opt.textContent = 'Немає читачів';
        opt.disabled = true;
        opt.selected = true;
        readerSelect.appendChild(opt);
    } else {
        const placeholder = document.createElement('option');
        placeholder.value = '';
        placeholder.textContent = 'Оберіть читача...';
        placeholder.disabled = true;
        placeholder.selected = true;
        readerSelect.appendChild(placeholder);

        currentReaders.forEach(reader => {
            const option = document.createElement('option');

            const name =
                reader.fullName ??
                reader.name ??
                ((reader.firstName || '') + ' ' + (reader.lastName || '')).trim();

            option.value = reader.id;
            option.textContent = name || ('Читач #' + reader.id);

            readerSelect.appendChild(option);
        });
    }

    modal.classList.remove('hidden');
}

function closeIssueModal() {
    const modal = document.getElementById('issue-modal');
    if (!modal) return;
    modal.classList.add('hidden');
}

async function onIssueFormSubmit(event) {
    event.preventDefault();

    const bookId = document.getElementById('issue-book-id').value;
    const readerId = document.getElementById('issue-reader').value;
    const days = document.getElementById('issue-days').value;

    if (!bookId || !readerId || !days) {
        showToast('Заповніть усі поля.', 'error');
        return;
    }

    const payload = {
        bookId: parseInt(bookId, 10),
        readerId: parseInt(readerId, 10),
        days: parseInt(days, 10)
    };

    try {
        const response = await fetch('/api/loans', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const text = await response.text();
            throw new Error('HTTP ' + response.status + ': ' + text);
        }

        closeIssueModal();
        await loadBooks();
        await loadRecentOperations();
        showToast('Книгу успішно видано читачу', 'success');
    } catch (err) {
        console.error('Помилка при видачі книги', err);
        showToast('Не вдалося видати книгу.', 'error');
    }
}

async function returnBook(book) {
    if (!confirm('Повернути цю книгу?')) {
        return;
    }

    try {
        const resp = await fetch(`/api/loans/by-book/${book.id}`);
        if (!resp.ok) {
            throw new Error('HTTP ' + resp.status);
        }

        const loans = await resp.json();
        const activeLoan = loans.find(l => !l.returned);

        if (!activeLoan) {
            showToast('Для цієї книги немає активної видачі.', 'info');
            return;
        }

        const respReturn = await fetch(`/api/loans/${activeLoan.id}/return`, {
            method: 'POST'
        });

        if (!respReturn.ok) {
            const text = await respReturn.text();
            throw new Error('HTTP ' + respReturn.status + ': ' + text);
        }

        await loadBooks();
        await loadRecentOperations();
        showToast('Книгу успішно повернуто', 'success');
    } catch (err) {
        console.error('Помилка при поверненні книги', err);
        showToast('Не вдалося повернути книгу.', 'error');
    }
}

// ================== ЖУРНАЛ ОПЕРАЦІЙ ==================

async function loadRecentOperations() {
    const tbody = document.getElementById('recent-operations-body');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="4">Завантаження...</td></tr>';

    try {
        const response = await fetch('/api/loans/recent?limit=50');
        if (!response.ok) {
            throw new Error('HTTP ' + response.status);
        }

        const loans = await response.json();

        let operations = [];

        loans.forEach(loan => {
            if (loan.issueDate) {
                operations.push({
                    readerName: loan.readerName,
                    bookName:  loan.bookName,
                    action:     'Видача',
                    date:       loan.issueDate
                });
            }

            if (loan.returned && loan.returnDate) {
                operations.push({
                    readerName: loan.readerName,
                    bookName:  loan.bookName,
                    action:     'Повернення',
                    date:       loan.returnDate
                });
            }
        });

        operations.sort((a, b) => new Date(b.date) - new Date(a.date));
        operations = operations.slice(0, 10);

        tbody.innerHTML = '';

        if (operations.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4">Поки немає операцій</td></tr>';
            return;
        }

        operations.forEach(op => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${op.readerName}</td>
                <td>${op.bookName}</td>
                <td>${op.action}</td>
                <td>${formatDateTime(op.date)}</td>
            `;
            tbody.appendChild(tr);
        });

    } catch (err) {
        console.error('Помилка при завантаженні операцій', err);
        tbody.innerHTML = '<tr><td colspan="4">Не вдалося завантажити дані</td></tr>';
    }
}

// ================== ХЕЛПЕРИ ==================

function formatDateTime(value) {
    if (!value) return '';
    const d = new Date(value);
    const pad = n => n.toString().padStart(2, '0');

    const day = pad(d.getDate());
    const month = pad(d.getMonth() + 1);
    const year = d.getFullYear();
    const hours = pad(d.getHours());
    const minutes = pad(d.getMinutes());
    const seconds = pad(d.getSeconds());

    return `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;
}

function openBookDetailsModal(book) {
    const modal = document.getElementById('book-details-modal');
    if (!modal) return;

    const nameEl = document.getElementById('book-details-name');
    const authorEl = document.getElementById('book-details-author');
    const publisherEl = document.getElementById('book-details-publisher');
    const statusEl = document.getElementById('book-details-status');
    const descrEl = document.getElementById('book-details-description');

    const issueEl = document.getElementById('book-details-issueDate');
    const dueEl = document.getElementById('book-details-dueDate');
    const returnEl = document.getElementById('book-details-returnDate');

    if (nameEl) nameEl.textContent = book.name ?? '';
    if (authorEl) authorEl.textContent = book.author ?? '';
    if (publisherEl) publisherEl.textContent = book.publisher ?? '';

    let statusText = book.issued ? 'Видана' : 'Доступна';
    let isOverdue = false;
    if (book.issued && book.dueDate) {
        const now = new Date();
        const due = new Date(book.dueDate);
        if (due < now) {
            isOverdue = true;
            statusText += ' (прострочена)';
        }
    }

    if (statusEl) {
        statusEl.textContent = statusText;
        statusEl.classList.toggle('status--overdue', isOverdue);
    }

    if (descrEl) descrEl.textContent = book.description ?? '';

    if (issueEl) issueEl.textContent = book.issueDate ? formatDateTime(book.issueDate) : '—';
    if (dueEl) dueEl.textContent = book.dueDate ? formatDateTime(book.dueDate) : '—';
    if (returnEl) returnEl.textContent = book.returnDate ? formatDateTime(book.returnDate) : '—';

    modal.classList.remove('hidden');
}

function closeBookDetailsModal() {
    const modal = document.getElementById('book-details-modal');
    if (!modal) return;
    modal.classList.add('hidden');
}

// ================== АДМІНКА: КОРИСТУВАЧІ ==================

async function loadUsers() {
    const tbody = document.getElementById('admin-users-table-body');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="4">Завантаження...</td></tr>';

    try {
        const res = await fetch('/api/users');
        if (!res.ok) throw new Error('HTTP ' + res.status);
        const data = await res.json();
        adminUsers = data;

        if (!data || data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4">Користувачів не знайдено</td></tr>';
            return;
        }

        tbody.innerHTML = data.map(u => {
            const isCurrent = currentUser && currentUser.username === u.username;
            const deleteBtnHtml = isCurrent
                ? ''   // свій акаунт – без кнопки видалення
                : `<button class="btn btn-light btn-sm"
                           data-action="delete" data-id="${u.id}">
                       Видалити
                   </button>`;

            return `
                <tr data-id="${u.id}">
                    <td>${u.id}</td>
                    <td>${u.username}</td>
                    <td>${u.role}</td>
                    <td>
                        <button class="btn btn-light btn-sm"
                                data-action="edit" data-id="${u.id}">
                            Редагувати
                        </button>
                        ${deleteBtnHtml}
                    </td>
                </tr>
            `;
        }).join('');

    } catch (e) {
        console.error('Помилка при завантаженні користувачів', e);
        tbody.innerHTML = '<tr><td colspan="4">Не вдалося завантажити користувачів</td></tr>';
    }
}

async function onUsersTableClick(event) {
    const btn = event.target.closest('button');
    if (!btn) return;

    const id = btn.dataset.id;
    const action = btn.dataset.action;
    if (!id || !action) return;

    if (action === 'edit') {
        const user = adminUsers.find(u => String(u.id) === String(id));
        if (!user) return;
        openUserModal(user);
    }
    else if (action === 'delete') {
        if (!confirm('Точно видалити користувача?')) return;

        try {
            const res = await fetch(`/api/users/${id}`, { method: 'DELETE' });

            if (res.ok) {
                await loadUsers();
                showToast('Користувача успішно видалено');
            } else {
                showToast('Не вдалося видалити користувача');
            }
        } catch (err) {
            console.error('Delete user error', err);
            showToast('Сталася помилка при видаленні');
        }
    }
}

function openUserModal(user) {
    const modal = document.getElementById('user-modal');
    if (!modal) return;

    const titleEl = document.getElementById('user-modal-title');
    const idInput = document.getElementById('user-id');
    const usernameInput = document.getElementById('user-username');
    const passwordInput = document.getElementById('user-password');

    if (user) {
        titleEl.textContent = 'Редагувати користувача';
        idInput.value = user.id;
        usernameInput.value = user.username || '';
    } else {
        titleEl.textContent = 'Додати бібліотекаря';
        idInput.value = '';
        usernameInput.value = '';
    }
    passwordInput.value = '';

    modal.classList.remove('hidden');
}

function closeUserModal() {
    const modal = document.getElementById('user-modal');
    if (!modal) return;
    modal.classList.add('hidden');
}

async function onUserFormSubmit(e) {
    e.preventDefault();

    const id = document.getElementById('user-id').value;
    const username = document.getElementById('user-username').value;
    const password = document.getElementById('user-password').value;
    const role = document.getElementById('user-role').value;

    const payload = {
        username,
        role
    };

    if (password.trim() !== "") {
        payload.password = password;
    }

    const url = id
        ? `/api/users/${id}`
        : `/api/users`;

    const method = id ? "PUT" : "POST";

    try {
        const resp = await fetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!resp.ok) {
            showToast('Помилка при збереженні користувача.', 'error');
            return;
        }

        closeUserModal();
        loadUsers();
        showToast('Користувача успішно збережено', 'success');
    } catch (err) {
        console.error("User save error", err);
    }
}

// ================== AUTH / LOGOUT ==================

async function onLogoutClick(e) {
    e.preventDefault();

    try {
        await fetch('/logout', {
            method: 'POST'
        });
    } catch (err) {
        console.error('Помилка при виході', err);
    } finally {
        window.location.href = '/login.html?logout';
    }
}

async function initAuthUi() {
    try {
        const resp = await fetch('/api/me');

        if (resp.status === 401 || resp.status === 403) {
            window.location.href = '/login.html';
            return;
        }

        if (!resp.ok) {
            throw new Error('HTTP ' + resp.status);
        }

        const data = await resp.json();

        currentUser.id = data.id;          // <- НОВЕ
        currentUser.username = data.username;
        currentUser.role = data.role;

        const userLabel = document.getElementById('current-user');
        if (userLabel) {
            userLabel.textContent = `Ви увійшли як: ${currentUser.username}`;
        }

        if (currentUser.role !== 'ADMIN') {
            const adminBtn = document.querySelector('.nav-btn[data-view="admin"]');
            const adminView = document.getElementById('view-admin');

            if (adminBtn) adminBtn.style.display = 'none';
            if (adminView) adminView.style.display = 'none';
        }

        const logoutBtn = document.getElementById('logout-btn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', onLogoutClick);
        }

    } catch (err) {
        console.error('Помилка при завантаженні /api/me', err);
        window.location.href = '/login.html';
    }
}

async function loadLogs() {
    const tbody = document.getElementById('admin-logs-table-body');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="4">Завантаження...</td></tr>';

    try {
        const res = await fetch('/api/logs?limit=50');
        if (!res.ok) {
            throw new Error('HTTP ' + res.status);
        }

        const logs = await res.json();
        renderLogs(logs);
    } catch (err) {
        console.error('Помилка при завантаженні логів', err);
        tbody.innerHTML = '<tr><td colspan="4">Не вдалося завантажити журнал</td></tr>';
    }
}

function renderLogs(logs) {
    const tbody = document.getElementById('admin-logs-table-body');
    if (!tbody) return;

    tbody.innerHTML = '';

    if (!logs || logs.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4">Журнал порожній</td></tr>';
        return;
    }

    logs.forEach(log => {
        const tr = document.createElement('tr');

        const time = log.createdAt ? formatDateTime(log.createdAt) : '';
        const user = log.username || '—';
        const action = log.action || '';          // ← тут
        const object = log.entityType
            ? `${log.entityType} #${log.entityId ?? ''}`
            : `#${log.entityId ?? ''}`;

        tr.innerHTML = `
            <td>${time}</td>
            <td>${user}</td>
            <td>${action}</td>
            <td>${object}</td>
        `;
        tbody.appendChild(tr);
    });
}
