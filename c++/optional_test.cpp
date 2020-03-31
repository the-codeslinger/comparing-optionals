#include <iostream>
#include <string>
#include <optional>

/**
 * The "test" cases use the same names as the test methods in the Java code, 
 * only styled to match C++ conventions.
 * 
 * The numbers in parenthesis, e.g. (1) or (2) match the Java code samples
 * in the respective test cases. Missing numbers mean missing features.
 * 
 * This pattern is used where applicable.
 */

/**
 * The sample class that will be used in all snippets.
 */
struct car 
{
    car(std::string man, std::string mod) 
        : manufacturer(man)
        , model(mod)
    {}

    bool operator==(const car& other) const 
    {
        return manufacturer == other.manufacturer && model == other.model;
    }

    friend std::ostream& operator<<(std::ostream& os, const car& car);

    std::string manufacturer;
    std::string model;
};

/**
 * The other sample class that will be used in a few snippets.
 */
struct truck
{
    truck(std::string man, std::string mod, std::string size) 
        : manufacturer(man)
        , model(mod)
        , size(size)
    {}

    bool operator==(const truck& other) const 
    {
        return manufacturer == other.manufacturer 
                && model == other.model 
                && size == other.size;
    }

    friend std::ostream& operator<<(std::ostream& os, const truck& car);

    std::string manufacturer;
    std::string model;
    std::string size;
};


std::ostream& operator<<(std::ostream& os, const car& car)
{
    os << car.manufacturer << " " << car.model;
    return os;
}


std::ostream& operator<<(std::ostream& os, const truck& truck)
{
    os << truck.size << " " << truck.manufacturer << " " << truck.model;
    return os;
}

static const auto loud_car = car{"Hyundai", "i30 N"};
static const auto reasonable_car = car{"Volkswagen", "Golf"};
static const auto huge_car = car{"Ford", "Raptor"};

/**
 * Demonstrate how std::nullopt can be used as a return value because std::optional
 * has a constructor that accepts this. Otherwise std::nullopt is not really useful
 * on its own.
 */
std::optional<car> make_empty_car() 
{
    return std::nullopt;
}

std::optional<car> produce_truck() 
{
    return std::make_optional(huge_car);
}

// Explicit types used for clarity.
void optional_of_null()
{
    std::cout << "optional_of_null" << std::endl
              << "----------------" << std::endl;
    
    // Does not work, requires object.
    // auto null_opt = std::make_optional(); 
    // Doesn't help us much by its own.
    std::nullopt_t null_opt = std::nullopt;
    // This on the other hand does.
    std::optional<car> null_opt_const = std::nullopt;
    // Or as a return from a method.
    std::optional<car> null_opt_method = make_empty_car();
    // Empty optional by creating a new std::optional manually.
    std::optional<car> null_opt_obj1 = std::optional<car>();
    // Empty optional by passing nullopt_t. Very long version of (2).
    std::optional<car> null_opt_obj2 = std::optional<car>(null_opt);

    // All work literally the same.
    if (!null_opt_const.has_value()) {
        std::cout << "null_opt_const has no value" << std::endl;
    }
    
    if (!null_opt_method.has_value()) {
        std::cout << "null_opt_method has no value" << std::endl;
    }
    
    if (!null_opt_obj1.has_value()) {
        std::cout << "null_opt_obj1 has no value" << std::endl;
    }

    if (!null_opt_obj2.has_value()) {
        std::cout << "null_opt_obj2 has no value" << std::endl;
    }
}

// auto types used for laziness. And because "auto" means "car" in German.
void optional_of_null_usage()
{
    std::cout << "optional_of_null_usage" << std::endl
              << "----------------------" << std::endl;

    // Given
    auto null_opt = make_empty_car();

    // When

    // (2) Get the actual value of the optional or another value if null.
    auto null_value_or = null_opt.value_or(reasonable_car);
    // (5) value() on empty optional throws.
    try {
        auto null_get = null_opt.value();
    } catch(const std::bad_optional_access& e) {
        std::cout << "value() throws if optional is empty" << std::endl;
    }
    

    // Then

    // (2)
    if (null_value_or == reasonable_car) {
        std::cout << "value_or() gets default object if optional is empty" << std::endl;
    }
}

void optional_of_value_usage()
{
    std::cout << "optional_of_value_usage" << std::endl
              << "-----------------------" << std::endl;

    // Given
    auto hyunday_opt = std::make_optional(loud_car);

    // When

    // (2)
    auto hyundai_value_or = hyunday_opt.value_or(reasonable_car);

    // Then

    // (2)
    if (hyundai_value_or == loud_car) {
        std::cout << "value_or() gets object if optional is not empty" << std::endl;
    }
}

void optional_of_value_other_usage()
{
    std::cout << "optional_of_value_other_usage" << std::endl
              << "-----------------------------" << std::endl;

    std::cout << "There's nothing std::optional has to offer here" << std::endl;
}

void bouncer_patterns()
{
    std::cout << "bouncer_patterns" << std::endl
              << "----------------" << std::endl;

    // Given
    auto hyunday_opt = std::make_optional(loud_car);

    // When

    // (1) Use has_value() condition. Most obvious and flexible approach.
    if (!hyunday_opt.has_value()) {
        // throw ...
        std::cout << "ERROR: If you see that then something is wrong" << std::endl;
    }
    auto value = hyunday_opt->manufacturer;
    std::cout << "Hyunday manufacturer value is " << value << " (surprise)" << std::endl;
}

void fake_repository_return_value()
{
    std::cout << "fake_repository_return_value" << std::endl
              << "----------------------------" << std::endl;

    std::cout << "Didn't bother to write helper methods; "  << std::endl
              << "std::optional has no map() or filter() like Java" << std::endl;
}

void direct_value_access()
{
    std::cout << "direct_value_access" << std::endl
              << "-------------------" << std::endl;

    // Given
    auto hyunday_opt = std::make_optional(loud_car);

    // When

    // (1) value() is not the only way to get to the value.
    auto manufacturer1 = hyunday_opt.value().manufacturer;
    // (2) you can also dereference it with *
    auto manufacturer2 = (*hyunday_opt).manufacturer;
    // (3) you can also dereference it with ->
    auto manufacturer3 = hyunday_opt->manufacturer;

    // Then
    if (manufacturer1 == manufacturer2 
            && manufacturer2 == manufacturer3
            && manufacturer3 == "Hyundai") {
        std::cout << "C++ offers some nicer ways to directly access the object" 
                  << std::endl;
    }
}

int main() 
{
    optional_of_null();
    std::cout << std::endl << std::endl;

    optional_of_null_usage();
    std::cout << std::endl << std::endl;

    optional_of_value_usage();
    std::cout << std::endl << std::endl;
    
    optional_of_value_other_usage();
    std::cout << std::endl << std::endl;
    
    bouncer_patterns();
    std::cout << std::endl << std::endl;
    
    fake_repository_return_value();
    std::cout << std::endl << std::endl;
    
    direct_value_access();
    std::cout << std::endl << std::endl;
    
    return 0;
}