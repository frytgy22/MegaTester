package ua.test.mega.tester.byCategory;


import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ua.test.mega.tester.byCategory.categories.PositionProcessorCategory;
import ua.test.mega.tester.junit.PositionProcessorJunitTest;
import ua.test.mega.tester.mockito.PositionProcessorMockitoTest;

@RunWith(Categories.class)
@Categories.IncludeCategory(PositionProcessorCategory.class)
@Suite.SuiteClasses({PositionProcessorJunitTest.class, PositionProcessorMockitoTest.class})
public class PositionProcessorCategoryTest {
}
