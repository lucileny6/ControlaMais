# Iniciar container do banco de dados

    docker run --rm -d --name mariadb-temp -e MARIADB_ROOT_PASSWORD=senha123 -e MARIADB_DATABASE=meubanco -e MARIADB_USER=usuario -e MARIADB_PASSWORD=senhauser -p 3306:3306 mariadb:latest