let currentBooks = [];
let currentReaders = [];

window.addEventListener('DOMContentLoaded', () => {
    const navButtons = document.querySelectorAll('.nav-btn');
    const views = document.querySelectorAll('.view');

    navButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const target = btn.dataset.view;
            navButtons.forEach(b => b.classList.toggle('active', b === btn));
            views.forEach(view => {
                view.classList.toggle('view--active', view.id === `view-${target}`);
            });
        });
    });

    // Кнопка "Додати книгу"
    const addBtn = document.getElementById('book-add-btn');
    if (addBtn) {
        addBtn.addEventListener('click', () => openBookModal());
    }

    // Сабміт форми
    const form = document.getElementById('book-form');
    if (form) {
        form.addEventListener('submit', onBookFormSubmit);
    }

    // Кнопка "Скасувати"
    const cancelBtn = document.getElementById('book-cancel-btn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', closeBookModal);
    }

    // Обробка кліків по кнопках "Редагувати / Видати / Видалити" в таблиці
    const tableBody = document.getElementById('books-table-body');
    if (tableBody) {
        tableBody.addEventListener('click', onBooksTableClick);
    }
    // кнопка "Додати читача"
    const readerAddBtn = document.getElementById('reader-add-btn');
    if (readerAddBtn) {
        readerAddBtn.addEventListener('click', () => openReaderModal());
    }

    // форма читача
    const readerForm = document.getElementById('reader-form');
    if (readerForm) {
        readerForm.addEventListener('submit', onReaderFormSubmit);
    }

    // кнопка "Скасувати" в модалці читача
    const readerCancelBtn = document.getElementById('reader-cancel-btn');
    if (readerCancelBtn) {
        readerCancelBtn.addEventListener('click', closeReaderModal);
    }

    // кліки по таблиці читачів (ти вже додав readersTableBody — просто перевір, що це є)
    const readersTableBody = document.getElementById('readers-table-body');
    if (readersTableBody) {
        readersTableBody.addEventListener('click', onReadersTableClick);
    }

    const filterBtn = document.getElementById('book-filter-btn');
    if (filterBtn) {
        filterBtn.addEventListener('click', applyBooksFilter);
    }

    const searchInput = document.getElementById('book-search');
    if (searchInput) {
        // Enter у полі пошуку
        searchInput.addEventListener('keyup', (e) => {
            if (e.key === 'Enter') {
                applyBooksFilter();
            }
        });
    }

    const onlyAvailableCheckbox = document.getElementById('books-only-available');
    if (onlyAvailableCheckbox) {
        onlyAvailableCheckbox.addEventListener('change', applyBooksFilter);
    }

    function applyBooksFilter() {
        const searchInput = document.getElementById('book-search');
        const query = searchInput ? searchInput.value.trim() : '';

        const onlyAvailableCheckbox = document.getElementById('books-only-available');
        const onlyAvailable = !!(onlyAvailableCheckbox && onlyAvailableCheckbox.checked);

        loadBooks({
            query: query || null,
            onlyAvailable
        });
    }

    if (filterBtn && searchInput) {
        filterBtn.addEventListener('click', (e) => {
            e.preventDefault();
            applyBooksFilter();
        });

        searchInput.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                applyBooksFilter();
            }
        });
    }


    loadBooks();
    loadReaders();
    loadRecentOperations();

    // обробка форми видачі
    const issueForm = document.getElementById('issue-form');
    if (issueForm) {
        issueForm.addEventListener('submit', onIssueFormSubmit);
    }

    const issueCancelBtn = document.getElementById('issue-cancel-btn');
    if (issueCancelBtn) {
        issueCancelBtn.addEventListener('click', closeIssueModal);
    }

    setInterval(loadRecentOperations, 10000);
});

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
        // режим редагування
        idInput.value = book.id;
        nameInput.value = book.name ?? '';
        authorInput.value = book.author ?? '';
        publisherInput.value = book.publisher ?? '';
        descriptionInput.value = book.description ?? '';
    } else {
        // режим створення нової
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
            const q = query.trim();
            // шукаємо і за назвою, і за автором
            params.append('name', q);
            params.append('author', q);
        }

        // “Лише доступні” = книги, які НЕ видані => issued=false
        if (onlyAvailable === true) {
            params.append('issued', 'false');
        }
        // якщо onlyAvailable === false або undefined — параметр не додаємо, отримаємо всі книги

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


async function loadReaders() {
    try {
        const response = await fetch('/api/readers');
        if (!response.ok) {
            throw new Error('HTTP ' + response.status);
        }
        const readers = await response.json();
        currentReaders = readers;
        renderReaders(currentReaders);   // ← додаємо рендер
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


function renderBooks(books) {
    const tbody = document.getElementById('books-table-body');
    if (!tbody) return;

    tbody.innerHTML = '';

    if (!books || books.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6">Книг не знайдено</td></tr>';
        return;
    }

    console.log('Books from backend:', books);

    books.forEach(book => {
        const title = book.name ?? '';
        const authorsText = book.author ?? '';
        const year = '';
        const genre = '';

        const statusText = book.issued ? 'Видана' : 'Доступна';

        const actionButtonHtml = book.issued
            ? `<button class="btn btn-warning btn-sm" data-action="return" data-id="${book.id}">Повернути</button>`
            : `<button class="btn btn-light btn-sm" data-action="issue" data-id="${book.id}">Видати</button>`;

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${title}</td>
            <td>${authorsText}</td>
            <td>${year}</td>
            <td>${genre}</td>
            <td>${statusText}</td>
            <td>
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
            alert('Ця книга вже видана.');
            return;
        }
        openIssueModal(book);
    } else if (action === 'return') {
        returnBook(book);
    }
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
        await loadReaders();    // оновлюємо таблицю
    } catch (err) {
        console.error('Помилка при збереженні читача', err);
        alert('Не вдалося зберегти читача.');
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

function openIssueModal(book) {
    const modal = document.getElementById('issue-modal');
    if (!modal) return;

    const bookIdInput = document.getElementById('issue-book-id');
    const bookTitleEl = document.getElementById('issue-book-title');
    const readerSelect = document.getElementById('issue-reader');
    const daysInput = document.getElementById('issue-days'); // <- тепер days, а не date

    if (!bookIdInput || !bookTitleEl || !readerSelect || !daysInput) {
        console.error('Не знайдено один із елементів модалки видачі');
        return;
    }

    // заповнюємо дані книги
    bookIdInput.value = book.id;
    bookTitleEl.textContent = book.name ?? '';

    // значення за замовчуванням для кількості днів
    daysInput.value = 14;

    // заповнюємо список читачів
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
    } catch (err) {
        console.error('Помилка при збереженні книги', err);
        alert('Не вдалося зберегти книгу');
    }
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
        alert('Заповніть усі поля.');
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
    } catch (err) {
        console.error('Помилка при видачі книги', err);
        alert('Не вдалося видати книгу');
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
        await loadBooks(); // щоб у випадку видалення читача списки в модалці видачі теж оновились
    } catch (err) {
        console.error('Помилка при видаленні читача', err);
        alert('Не вдалося видалити читача.');
    }
}
async function returnBook(book) {
    if (!confirm('Повернути цю книгу?')) {
        return;
    }

    try {
        // 1. Знаходимо активну позику для цієї книги
        const resp = await fetch(`/api/loans/by-book/${book.id}`);
        if (!resp.ok) {
            throw new Error('HTTP ' + resp.status);
        }

        const loans = await resp.json();
        const activeLoan = loans.find(l => !l.returned);

        if (!activeLoan) {
            alert('Для цієї книги немає активної видачі.');
            return;
        }

        // 2. Викликаємо повернення за loanId
        const respReturn = await fetch(`/api/loans/${activeLoan.id}/return`, {
            method: 'POST'
        });

        if (!respReturn.ok) {
            const text = await respReturn.text();
            throw new Error('HTTP ' + respReturn.status + ': ' + text);
        }

        // 3. Оновлюємо дані без перезавантаження сторінки
        await loadBooks();
        await loadRecentOperations();

    } catch (err) {
        console.error('Помилка при поверненні книги', err);
        alert('Не вдалося повернути книгу.');
    }
}

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

        // будуємо масив операцій: ISSUE + RETURN
        let operations = [];

        loans.forEach(loan => {
            // 1) завжди додаємо операцію "Видача", якщо є дата видачі
            if (loan.issueDate) {
                operations.push({
                    readerName: loan.readerName,
                    bookName:  loan.bookName,
                    action:     'Видача',
                    date:       loan.issueDate
                });
            }

            // 2) якщо книга повернута — додаємо окремий запис "Повернення"
            if (loan.returned && loan.returnDate) {
                operations.push({
                    readerName: loan.readerName,
                    bookName:  loan.bookName,
                    action:     'Повернення',
                    date:       loan.returnDate
                });
            }
        });

        // сортуємо за датою (найсвіжіші зверху)
        operations.sort((a, b) => new Date(b.date) - new Date(a.date));

        // залишаємо тільки перші 10
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

function formatDateTime(value) {
    if (!value) return '';
    const d = new Date(value);
    // dd.MM.yyyy HH:mm:ss
    const pad = n => n.toString().padStart(2, '0');

    const day = pad(d.getDate());
    const month = pad(d.getMonth() + 1);
    const year = d.getFullYear();
    const hours = pad(d.getHours());
    const minutes = pad(d.getMinutes());
    const seconds = pad(d.getSeconds());

    return `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;
}



