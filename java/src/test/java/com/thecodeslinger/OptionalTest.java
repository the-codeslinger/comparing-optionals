package com.thecodeslinger;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * The test cases use the same names as the test methods in the C++ code, only styled
 * to match Java conventions.
 *
 * The numbers in parenthesis, e.g. (1) or (2) match the C++ code samples
 * in the respective test cases. Missing numbers mean missing features.
 *
 * This pattern is used where applicable.
 */
class OptionalTest {

    private static final Car LOUD_CAR = new Car("Hyundai", "i30 N");
    private static final Car REASONABLE_CAR = new Car("Volkswagen", "Golf");
    private static final Car HUGE_CAR = new Car("Ford", "Raptor");

    /**
     * A custom class to demonstrate the use of {@link Optional}
     */
    @Data
    @AllArgsConstructor
    static class Car {
        private String manufacturer;
        private String model;
    }

    /**
     * Another custom class to demonstrate the use of {@link Optional}
     */
    @Data
    @AllArgsConstructor
    static class Truck {
        private String manufacturer;
        private String model;
        private String size;
    }

    @Test
    void optionalOfNull() {
        // When

        // Optional.of expects non-null.
        var nullPtrException = catchThrowable(() -> Optional.of(null));
        // Optional.ofNullable works with null.
        var nullOpt = Optional.ofNullable(null);
        // Equivalent to above; usually used as return value.
        var emptyOpt = Optional.empty();

        // Then
        assertThat(nullPtrException).isInstanceOf(NullPointerException.class);
        assertThat(nullOpt).isEmpty();
        assertThat(emptyOpt).isEmpty();
    }

    @Test
    void optionalOfNullUsage() {
        // Given
        var nullOpt = Optional.<Car>empty();

        // When

        // (1) Get the optional or another optional if null.
        var nullOr = nullOpt.or(OptionalTest::produceTruck);
        // (2) Get the actual value of the optional or another value if null.
        var nullOrElse = nullOpt.orElse(REASONABLE_CAR);
        // (3) Get the actual value of the optional or throw an exception if null. "Bouncer pattern".
        var nullOrElseThrow = catchThrowable(() -> nullOpt.orElseThrow(IllegalArgumentException::new));
        // (4) Perform an action if the optional is not empty.
        var nullIfPresent = catchThrowable(() -> nullOpt.ifPresent(OptionalTest::haveFun));
        // (5) get() on empty optional throws.
        var nullGet = catchThrowable(() -> nullOpt.get());

        // Then

        // (1)
        assertThat(nullOr)
                .isInstanceOf(Optional.class)
                .isNotEmpty();
        assertThat(nullOr.get()).isEqualTo(HUGE_CAR);
        // (2)
        assertThat(nullOrElse).isEqualTo(REASONABLE_CAR);
        // (3)
        assertThat(nullOrElseThrow).isInstanceOf(IllegalArgumentException.class);
        // (4) Null because ifPresent does not call haveFun if optional is empty.
        assertThat(nullIfPresent).isNull();
        // (5)
        assertThat(nullGet).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void optionalOfValueUsage() {
        // Given
        var hyundaiOpt = Optional.of(LOUD_CAR);

        // Expect
        assertThat(hyundaiOpt).isNotEmpty();

        // When

        // (1) This time the LOUD_CAR is returned.
        var hyundaiOr = hyundaiOpt.or(OptionalTest::produceTruck);
        // (2) Again, LOUD_CAR.
        var hyundaiOrElse = hyundaiOpt.orElse(REASONABLE_CAR);
        // (3) No exception.
        var hyundaiOrElseThrow = hyundaiOpt.orElseThrow(IllegalArgumentException::new);
        // (4) haveFun() is called, hence an exception is thrown.
        var hyundaiIfPresent = catchThrowable(() -> hyundaiOpt.ifPresent(OptionalTest::haveFun));

        // Then

        // (1)
        assertThat(hyundaiOr).isEqualTo(hyundaiOpt);
        // (2)
        assertThat(hyundaiOrElse).isEqualTo(LOUD_CAR);
        // (3)
        assertThat(hyundaiOrElseThrow).isEqualTo(LOUD_CAR);
        // (4)
        assertThat(hyundaiIfPresent)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Hyundai i30 N");
    }

    @Test
    void optionalOfValueOtherUsage() {
        // Given
        var nullOpt = Optional.<Car>empty();
        var hyundaiOpt = Optional.of(LOUD_CAR);

        // When

        // (1) Convert to another type. This example only extract a property.
        var manufacturer = hyundaiOpt.map(Car::getManufacturer);
        // (2) The same, only with an empty nullable.
        var nullMap = nullOpt.map(Car::getManufacturer);
        // (3) Only let certain values pass.
        var filtered = hyundaiOpt.filter(car -> car.getModel().equalsIgnoreCase("i30 N"));


        // Then

        // (1)
        assertThat(manufacturer).isEqualTo(Optional.of("Hyundai"));
        // (2)
        assertThat(nullMap).isEmpty();
        // (3)
        assertThat(filtered).isEqualTo(hyundaiOpt);
    }

    @Test
    void bouncerPatterns() {
        // Given
        var hyundaiOpt = Optional.of(LOUD_CAR);


        // (1) Use isPresent() or isEmpty() condition. Most obvious and flexible approach.
        if (hyundaiOpt.isEmpty()) {
            // throw ...
            assertThat(false).isTrue(); // Not called.
        }
        var value1 = hyundaiOpt.get();
        assertThat(value1).isEqualTo(LOUD_CAR);

        // (2) Use orElseThrow(). No condition and immediately returns value.
        var value2 = hyundaiOpt.orElseThrow(NullPointerException::new);
        assertThat(value2).isEqualTo(LOUD_CAR);
    }

    @Test
    void fakeRepositoryReturnValue() {
        // Given
        var userSearchString = "raptor";

        // When
        var truck = repository_findByModel(userSearchString)
                .map(OptionalTest::fromCar)
                .orElseThrow(IllegalArgumentException::new);

        // Then
        assertThat(truck).isInstanceOf(Truck.class);
    }

    @Test
    void directValueAccess() {
        // Given
        var hyundaiOpt = Optional.of(LOUD_CAR);

        // When

        // (1) get() is the only way to get to the value.
        var manufacturer = hyundaiOpt.get().getManufacturer();

        // Then

        // (1)
        assertThat(manufacturer).isEqualTo("Hyunday");

    }

    /*********** Helpers ***********/

    static Optional<Car> produceTruck() {
        return Optional.of(new Car("Ford", "Raptor"));
    }

    static void haveFun(final Car car) {
        if (null == car) {
            throw new IllegalArgumentException("This ain't not fun");
        }

        // Used to be able to verify the paths taken in this method.
        throw new IllegalStateException(car.getManufacturer() + " " + car.getModel());
    }

    static Optional<Car> repository_findByModel(final String model) {
        if (model.equalsIgnoreCase("Raptor")) {
            return Optional.of(HUGE_CAR);
        }
        return Optional.empty();
    }

    static Truck fromCar(final Car car) {
        return new Truck(car.getManufacturer(), car.getModel(), "HUGE");
    }
}
