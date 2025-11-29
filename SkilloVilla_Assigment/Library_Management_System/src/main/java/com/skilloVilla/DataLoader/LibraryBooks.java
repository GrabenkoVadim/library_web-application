//package com.skilloVilla.DataLoader;
//
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.skilloVilla.Entity.Book;
//import com.skilloVilla.Repository.BookRepository;
//
//@Component
//public class LibraryBooks {
//
//	/*
//	 * This Class is used for per loading data in the data base of Book
//	 * here 22 books are present in the data base
//	 * data base name :- libraryManagmentSystem
//	 */
//
//
//	 @Autowired
//	    private BookRepository bookRepository;
//
//	    @PostConstruct
//	    public void loadBooks() {
//	        Book book1 = new Book();
//	        book1.setBookName("Don");
//	        book1.setBookAuthor("Miguel de Cervantes");
//	        book1.setBookPublisher("Jaico Publishing House");
//            book1.setBookDescription("Established in 1946, Jaico was India’s first publisher of paperback books in the English language. Founded by Jaman Shah, it was initially a book distribution company. He saw a demand for affordable books and initiated Jaico’s publishing program. Headquartered in Mumbai, Jaico is known for its self-help, religion and philosophy, mind/body/spirit and business titles. It also publishes travel, current affairs, biography and popular science books. Jaico has a translation division that translates English into nine Indian languages. Some of its famous authors are Robin Sharma, Deepak Chopra, Khushwant Singh, Brian Tracy and John Maxwell. One of Jaico’s well known titles is ‘The Monk who Sold His Ferrari’ by Robin Sharma.  ");
//
//            bookRepository.save(book1);
//
//	        Book book2 = new Book();
//	        book2.setBookName("Alice's Adventures in Wonderland");
//	        book2.setBookAuthor("Lewis Carroll");
//	        book2.setBookPublisher("Westland Publications");
//            book2.setBookDescription("Westland started as an amalgamation of two companies, EastWest Books (Madras) Pvt Ltd. and Westland Books Pvt Ltd. EastWest Books, established in 1996, was a successor company to the south India operations of Affiliated East West Press Private Limited, New Delhi, established in 1962. Then it was a subsidiary of Trent which is the retail arm of the Tata group, a global enterprise headquartered in India. Amazon acquired it in 2016 enabling Westland to grow their physical and digital book business. It has a long history of publication and distribution but it has shifted its focus to publishing in the last 9 years. Westland is one of the largest English language trade publishers in India. Bestselling authors of the company include Amish Tripathi, Ashok Banker, Rujuta Diwekar and Harsha Bhogle. Westland accepts submissions only through literary agents. Based in Delhi, the company’s popular titles include ‘The Immortals of Meluha’ by Amish Tripathi, the first book of the ‘Shiva Trilogy’ and the fastest selling book series in the history of Indian publishing. Westland prints in English and almost all major Indian regional languages.  ");
//
//            bookRepository.save(book2);
//
//	        Book book3 = new Book();
//	        book3.setBookName("The Adventures of Huckleberry Finn");
//	        book3.setBookAuthor("Mark Twain");
//	        book3.setBookPublisher("Penguin Random House India");
//	        book3.setBookDescription("Penguin Random House India, earlier known as Penguin India is a subsidiary of the global publishing house, Penguin Random House. Formed as Penguin India in 1985, it is the largest English language trade publisher in the continent. Penguin Random House India publishes classic fiction, modern fiction, biographies, sports, travel, business and illustrated children’s books. The books are available in Hindi, Tamil, Marathi, Gujarati, Telugu, Kannada, Malayalam, Bengali and Urdu.");
//
//	        bookRepository.save(book3);
//
//	        Book book4 = new Book();
//	        book4.setBookName("Treasure Island");
//	        book4.setBookAuthor("Robert Louis Stevenson");
//	        book4.setBookPublisher("Roli Books");
//	        book4.setBookDescription("A mid-sized publication firm founded by Pramod Kapoor in 1978, Roli Books is known for beautifully crafted books on Indian art, photography and cuisines. Pramod Kapoor was conferred with France’s highest civilian award ‘Knight of the Legion of Honor’ in 2016 for his lifelong commitment to promoting Indian heritage through books. If you are looking to create a rich coffee table book that is a mixture of art and literature, Roli Books is your publisher.  ");
//
//	        bookRepository.save(book4);
//
//	        Book book5 = new Book();
//	        book5.setBookName("Pride and Prejudice");
//	        book5.setBookAuthor("Jane Austen");
//	        book5.setBookPublisher("Rupa Publications");
//	        book5.setBookDescription("Rupa Publications was started in 1936 by D. Mehra. Over 95% of the Rupa catalogue is converted into e-books, a strategy that effectively helps reach a wider audience. The Delhi based Rupa Publications has an eclectic mix of fiction, non-fiction and children’s books. They have titles in history, business, politics and a host of memoirs. Authors at this distinguished publishing house consist of Ruskin Bond, APJ Abdul Kalam, Pranab Mukherjee and Chetan Bhagat. ");
//
//	        bookRepository.save(book5);
//
//	        Book book6 = new Book();
//	        book6.setBookName("Wuthering Heights");
//	        book6.setBookAuthor("Emily Brontë");
//	        book6.setBookPublisher("Hachette India");
//	        book6.setBookDescription("Hachette India is the Indian arm of Hachette UK. A relatively young publishing presence, it was launched in 2009. Situated in Gurgaon, Hachette India publishes general, literary and commercial fiction, children’s and reference books as well as non-fiction, covering memoirs, self help, travel, history, business, popular culture, lifestyle and sport. Successful titles at the house are ‘Turbulence’ by Samit Basu, ‘Durbar’ by Tavleen Singh and Amit Varma’s ‘My Friend Sancho.’  ");
//
//	        bookRepository.save(book6);
//
//	        Book book7 = new Book();
//	        book7.setBookName("Jane Eyre");
//	        book7.setBookAuthor("Charlotte Brontë");
//	        book7.setBookPublisher("Aleph Book Company");
//	        book7.setBookDescription("A newbie in the Indian publishing scene, Aleph Book Company was founded in May 2011 by David Davidar with R.K. Mehra and Kapish Mehra of Rupa Publications India. Working out of Delhi, Aleph is a selective publishing house focusing on quality rather than quantity with around 40 titles published annually in areas like fiction, history, autobiography, travel, business, popular culture and non-fiction. The author list at Aleph has names like Shashi Tharoor, Devdutt Patnaik, Valmik Thapar, Vikram Seth and Khushwant Singh.  ");
//
//	        bookRepository.save(book7);
//
//	        Book book8 = new Book();
//	        book8.setBookName("Moby Dick");
//	        book8.setBookAuthor("Herman Melville");
//	        book8.setBookPublisher("HarperCollins Publishers India");
//	        book8.setBookDescription("No publishing list is complete without mentioning HarperCollins. HarperCollins Publishers India is a wholly owned subsidiary of HarperCollins Worldwide. It was launched in 1992 in Noida. The company dazzles with authors like Kiran Nagarkar, Tarun Tejpal and Arvind Adiga. Adiga also won the Man Booker Prize in 2008 for ‘The White Tiger' They are one of the few publishers who are interested in publishing poetry. Other than poems, they also publish on a range of topics both fiction and non-fiction. They also are keen to have a growing list of writers for their Harper Hindi program");
//
//	        bookRepository.save(book8);
//
//
//	        Book book9 = new Book();
//	        book9.setBookName("	The Scarlet Letter");
//	        book9.setBookAuthor("Nathaniel Hawthorne");
//	        book9.setBookPublisher("Pan Macmillan India");
//	        book9.setBookDescription("Pan Macmillan India is a part of Macmillan Publishers, a global trade publishing group. Another new kid in the Indian publishing bunch, Pan Macmillan started publishing operations in August 2010. Pan Macmillan India covers genres like fiction and non-fiction across various sub genres. One of its most famous books was ‘India after Gandhi’ by Ramachandra Guha. Pan Macmillan India has, until recently, been publishing only under the Picador imprint. The company has now added Pan and Macmillan, two new imprints to cover its local commercial fiction and non-fiction publishing.  ");
//
//	        bookRepository.save(book9);
//
//	        Book book10 = new Book();
//	        book10.setBookName("Gulliver's Travels");
//	        book10.setBookAuthor("Jonathan Swift	");
//	        book10.setBookPublisher("Scholastic India");
//	        book10.setBookDescription("If you are writing an academic or children’s illustrated book, Scholastic India is the publisher you should go to. Started in 1997, Scholastic India is a publisher and distributor of children’s books. A famous title from Scholastic India is ‘The Hunger Games’ by Suzanne Collins. The Gurgaon based publishing house publishes books on educational technology, educational services and children’s media. They print in 45 languages and reach more than 150 countries. Gulzar, Paro Anand and Anushka Ravishankar are some of their renowned authors.  ");
//
//	        bookRepository.save(book10);
//
//	        Book book11 = new Book();
//	        book11.setBookName("The Pilgrim's Progress");
//	        book11.setBookAuthor("John Bunyan");
//	        book11.setBookPublisher("24by7Publishing");
//	        book11.setBookDescription("The traditional route of sending your manuscript to a publishing house may not be everyone’s cup of tea. So if that is the case, you can always opt for self-publishing. Self-publishing is when the author of any book chooses to publish his work without the involvement of a third party publisher. You can create your own cover, book and control your content without any interference. 24by7Publishing is one of India’s famous self -publishing companies. This Kolkata based company lets you in the driver seat for various packages where you decide how to package your book.");
//
//	        bookRepository.save(book11);
//
//	        Book book12 = new Book();
//	        book12.setBookName("A Christmas Carol");
//	        book12.setBookAuthor("Charles Dickens");
//	        book12.setBookPublisher("Cinnamon Teal Publishing");
//	        book12.setBookDescription("Founded in 2007, Cinnamon Teal is a self-publishing company that has strict standards which gives their books a certain quality that other platforms might lack. Their publishing philosophy is unique and has helped them achieved the ‘Young Creative Entrepreneur Award’ in the field of self-publishing in 2010.  ");
//
//	        bookRepository.save(book12);
//
//	        Book book13 = new Book();
//	        book13.setBookName("David Copperfield");
//	        book13.setBookAuthor("Charles Dickens");
//	        book13.setBookPublisher(" Become Shakespeare");
//	        book13.setBookDescription("Become Shakespeare is a self-publishing brand situated in Bombay. They have extensive packages suitable for different needs and budget. They have published nearly 1050 books till date. They offer a host of services like getting your books in retail stores, public relations and handling social media. They even set up a team dedicated to you that ensures it is getting marketed and promoted well");
//
//	        bookRepository.save(book13);
//
//	        Book book14 = new Book();
//	        book14.setBookName("A Tale of Two Cities");
//	        book14.setBookAuthor("Charles Dickens");
//	        book14.setBookPublisher("Leadstart Publishing");
//	        book14.setBookDescription("Leadstart Publishing is a progressive company with titles in health, biography, travel, fiction, religion, health and children’s books. They have Digital Leadstart to reach the readers online. This Mumbai based publishing house also has a literary agency that works for writers either looking to publish or resell or pitch content to other mediums. The most recent bestseller of Leadstart is Asura by Anand Neelakanthan. The author is also writing the prequel to fantasy fiction super hit Baahubali");
//
//	        bookRepository.save(book14);
//
//	        Book book15 = new Book();
//	        book15.setBookName("Little Women");
//	        book15.setBookAuthor("Louisa May Alcott");
//	        book15.setBookPublisher("Fingerprint Publishing");
//	        book15.setBookDescription("Fingerprint was founded in 2012. Located in Delhi, it’s a publishing house owned by Prakash Books, a major distributor in India. Their current bestsellers are ‘Seven Uncommoners’ by Ridhima Verma and ‘The Sari Shop Widow’ by Shobhan Bantwal. Fingerprint’s intent is to publish interesting paperbacks that can be sold country wide with the backing of Prakash Books.  ");
//
//	        bookRepository.save(book15);
//
//	        Book book16 = new Book();
//	        book16.setBookName("Great Expectations");
//	        book16.setBookAuthor("Charles Dickens");
//	        book16.setBookPublisher("APK Publishers");
//	        book16.setBookDescription("APK Publishers was started because it was essential to give first time writers the opportunity to share their stories. Located in Pune, this ‘truly English’ publishing house has a mission to help first time writers. Launched in 2008 by Prashant and Anagha Karhade, APK has published 50 books till date. Their top sellers are ‘Ripples’ an anthology of 48 short stories by 26 Indian women writers, ‘Alice’ series by Megha Rao and ‘Moonstone: The Incredible One Hour’ by Atul Gupta.");
//
//	        bookRepository.save(book16);
//
//	        Book book17 = new Book();
//	        book17.setBookName("Oliver Twist");
//	        book17.setBookAuthor("Louisa May Alcott");
//	        book17.setBookPublisher("Petals Publishers");
//	        book17.setBookDescription("Started by Harpreet Makkar in 2013, Petals is a multi-faceted publishing house that provides access to digital marketing, editing and distribution for all writers. With its office in Ludhiana, Petals is looking to blur lines by offering the option of self-publishing along with traditional publishing depending on the needs of the author. They may not have any big names as their authors but they have a solid collection across genres like romance, fantasy and mystery. ");
//
//	        bookRepository.save(book17);
//
//	        Book book18 = new Book();
//	        book18.setBookName("The Hobbit, or, There and Back Again");
//	        book18.setBookAuthor("J.R.R. Tolkien");
//	        book18.setBookPublisher("Srishti Publishers");
//	        book18.setBookDescription("Srishti was founded in Delhi 18 years ago with the idea of giving a platform to first time writers and this company should definitely be on your list if you are considering sending a manuscript to a publisher. Publishing almost 350 titles annually including translations of works in languages like Oriya, Malayalam, Bengali, Punjabi, Urdu, Tamil and Hindi, Srishti has genres like fantasy fiction, self help, philosophy, astrology, espionage thrillers and culture. Srishti has been the debut platform for authors like Preeti Shenoy, Durjoy Datta and Sudeep Nagarkar. ‘Life is What You Make It’ by Preeti Shenoy was ranked No 1 in 2014 by Nielsen Book Scan");
//
//	        bookRepository.save(book18);
//
//	        Book book19 = new Book();
//	        book19.setBookName("Frankenstein, or, the Modern Prometheus");
//	        book19.setBookAuthor("Mary Shelley");
//	        book19.setBookPublisher("APK Publishers");
//	        book19.setBookDescription("APK Publishers was started because it was essential to give first time writers the opportunity to share their stories. Located in Pune, this ‘truly English’ publishing house has a mission to help first time writers. Launched in 2008 by Prashant and Anagha Karhade, APK has published 50 books till date. Their top sellers are ‘Ripples’ an anthology of 48 short stories by 26 Indian women writers, ‘Alice’ series by Megha Rao and ‘Moonstone: The Incredible One Hour’ by Atul Gupta.  ");
//
//	        bookRepository.save(book19);
//
//	        Book book20 = new Book();
//	        book20.setBookName("Frankenstein, or, the Modern Prometheus	");
//	        book20.setBookAuthor("Mary Shelley");
//	        book20.setBookPublisher("Pustak Mahal");
//	        book20.setBookDescription("Formed in 1974, Pustak Mahal is the pioneer for language learning and supplementary books for children. With nearly 1500 titles and 50 on the bestseller’s lists, the company produces books on general information, children, language, education, career guidance and self-management. The company was one of India’s first to digitalize its publications. ");
//
//	        bookRepository.save(book20);
//
//	        Book book21 = new Book();
//	        book21.setBookName("Uncle Tom's Cabin");
//	        book21.setBookAuthor("Harriet Beecher Stowe");
//	        book21.setBookPublisher("S. Chand Publishing");
//	        book21.setBookDescription("Formed by Shyam Lal Gupta in 1939 in Delhi, S. Chand has now become the second largest education publisher in India. Joining the digital bandwagon, S. Chand is offering online platforms for exam preparations, designing activities for children and a home tutoring service. If you are interested in writing for higher education, competitive and children’s books then submit a manuscript to S. Chand. They publish books in almost all regional languages. That’s our extensive list of 21 top book publishers in India, which are suitable for a variety of genres. Before you decide anything, do give it a thought if you should go for a traditional publisher or self-publish. Every side has its pros and cons, but it is for you to know what meets your needs and choose accordingly. What did you think of the book publishing houses in India featured in this post? Are there any others we should add to the list? Please tell us in the comments box below!");
//
//	        bookRepository.save(book21);
//
//	        Book book22 = new Book();
//	        book22.setBookName("Crime and Punishment");
//	        book22.setBookAuthor("Fyodor Dostoyevsky");
//	        book22.setBookPublisher("APK Publishers");
//	        book22.setBookDescription("APK Publishers was started because it was essential to give first time writers the opportunity to share their stories. Located in Pune, this ‘truly English’ publishing house has a mission to help first time writers. Launched in 2008 by Prashant and Anagha Karhade, APK has published 50 books till date. Their top sellers are ‘Ripples’ an anthology of 48 short stories by 26 Indian women writers, ‘Alice’ series by Megha Rao and ‘Moonstone: The Incredible One Hour’ by Atul Gupta");
//
//	        bookRepository.save(book22);
//
////	        Book book1 = new Book();
////	        book1.setBookName("");
////	        book1.setBookAuthor("");
////	        book1.setBookPublisher("");
////	        book1.setBookDescription("");
////	        bookRepository.save(book1);
//
//
//
//	    }
//
//}
