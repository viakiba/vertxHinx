mvn clean package -DskipTests
mkdir output
cp -r ./config ./output/
cp -r ./target/libs ./output/
cp ./target/*.jar ./output/
cp ./docs/shell/*.sh ./output/