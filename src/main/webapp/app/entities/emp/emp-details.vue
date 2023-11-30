<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="emp">
        <h2 class="jh-entity-heading" data-cy="empDetailsHeading"><span>Emp</span> {{ emp.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>First Name</span>
          </dt>
          <dd>
            <span>{{ emp.firstName }}</span>
          </dd>
          <dt>
            <span>Last Name</span>
          </dt>
          <dd>
            <span>{{ emp.lastName }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link v-if="emp.id" :to="{ name: 'EmpEdit', params: { empId: emp.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./emp-details.component.ts"></script>
