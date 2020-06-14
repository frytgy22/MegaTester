package ua.test.mega.tester.byCategory;


import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ua.test.mega.tester.byCategory.categories.AccountProcessorCategory;
import ua.test.mega.tester.junit.AccountProcessorJunitTest;
import ua.test.mega.tester.mockito.AccountProcessorMockitoTest;

@RunWith(Categories.class)
@Categories.IncludeCategory(AccountProcessorCategory.class)
@Suite.SuiteClasses({AccountProcessorJunitTest.class, AccountProcessorMockitoTest.class})
public class AccountProcessorCategoryTest {
}
