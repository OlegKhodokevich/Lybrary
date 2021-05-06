package by.htp.library.controller.command.impl;

import by.htp.library.bean.Book;
import by.htp.library.bean.creator.BookCreator;
import by.htp.library.controller.CreatorForController;
import by.htp.library.controller.command.Command;
import by.htp.library.controller.exeption.ControllerLibraryException;
import by.htp.library.controller.parser.ParserRequest;
import by.htp.library.controller.validator.ValidateAdmin;
import by.htp.library.dao.exception.DAOLibraryException;
import by.htp.library.service.LibraryService;
import by.htp.library.service.exception.ServiceLibraryException;

public class EditeBook implements Command {
	private static final String DELIMITER = "/";

	@Override
	public String execute(String request) throws ControllerLibraryException {

		String responce = null;

		CreatorForController creator = CreatorForController.getInstanceCreator();
		boolean permission = ValidateAdmin.validateAdmin(request);

		ParserRequest parserRequest = creator.getParserRequest();
		request = parserRequest.parseAndDeliteParamAdminFromRequest(request);

		if (permission) {
			String name = null;
			String autor = null;

			Book book = null;

			int lastIndex = request.indexOf(DELIMITER);
			name = request.substring(0, lastIndex);
			request = request.substring(lastIndex + 1);

			lastIndex = request.indexOf(DELIMITER);
			autor = request.substring(0, lastIndex);
			request = request.substring(lastIndex + 1);

			BookCreator bookCreator = creator.getBookCreator();

			try {
				book = bookCreator.createBookFromString(request);
			} catch (DAOLibraryException e1) {
				throw new ControllerLibraryException(e1);
			}

			LibraryService libraryService = creator.getLibraryService();
			boolean result = false;

			try {
				result = libraryService.editeBook(name, autor, book);
			} catch (ServiceLibraryException e) {
				throw new ControllerLibraryException(e);
			}

			if (result) {
				responce = "Book" + book.getNameBook() + " has added.";
			} else {
				responce = "Book with that name = " + name + " and autor = " + autor + "  is not exist.";
			}

		} else {
			responce = "You did not sign in as admin. ";
		}

		return responce;
	}
}
